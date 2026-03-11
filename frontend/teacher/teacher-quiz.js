// teacher/teacher-quiz.js
console.log("teacher-quiz.js loaded");

let teacherId = null;
let questionCount = 0;

function getTeacherId() {
    // Just return the username - backend will map it
    const token = localStorage.getItem('token');
    if (!token) return null;
    
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        return payload.sub; // Returns 'teacherA'
    } catch (error) {
        console.error('Token decode error:', error);
        return null;
    }
}

function goToDashboard() {
    window.location.href = 'dashboard.html';
}

function loadQuizzes() {
    const teacherUsername = getTeacherId(); // This is 'teacherA'
    if (!teacherUsername) {
        alert("Please login again");
        window.location.href = '../login.html';
        return;
    }
    
    console.log("Using teacherUsername:", teacherUsername);
    
    const branch = document.getElementById('branchFilter')?.value || '';
    const subject = document.getElementById('subjectFilter')?.value || '';
    
    // Send username, backend will convert to ID
    let url = `http://localhost:8080/teacher/quiz?teacherUsername=${encodeURIComponent(teacherUsername)}`;
    if (branch) url += `&branch=${encodeURIComponent(branch)}`;
    if (subject) url += `&subject=${encodeURIComponent(subject)}`;
    
    console.log('Fetching from:', url);
    
    fetch(url, {
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        }
    })
    .then(response => {
        console.log('Response status:', response.status);
        if (response.status === 404) {
            throw new Error('Teacher not found');
        }
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
    .then(quizzes => {
        console.log('Quizzes received:', quizzes);
        displayQuizzes(quizzes);
    })
    .catch(error => {
        console.error('Error loading quizzes:', error);
        document.getElementById('quizzesContainer').innerHTML = `
            <div style="color:red; padding:20px; border:1px solid red; margin:10px;">
                <h4>Error loading quizzes</h4>
                <p>${error.message}</p>
                <p>Username: ${teacherUsername}</p>
                <button onclick="testDebugEndpoint('${teacherUsername}')">Debug Mapping</button>
                <button onclick="loadQuizzes()">Retry</button>
            </div>
        `;
    });
}

function debugToken() {
    const token = localStorage.getItem('token');
    if (!token) {
        alert("No token found");
        return;
    }
    
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        alert("Token payload:\n\n" + JSON.stringify(payload, null, 2));
    } catch (error) {
        alert("Token decode error: " + error.message);
    }
}

// Rest of the functions remain the same...
function displayQuizzes(quizzes) {
    const container = document.getElementById('quizzesContainer');
    
    if (!quizzes || quizzes.length === 0) {
        container.innerHTML = '<p>No quizzes found. Create your first quiz!</p>';
        return;
    }
    
    let html = '<table border="1" style="width:100%;">';
    html += '<tr><th>Title</th><th>Subject</th><th>Branch</th><th>Semester</th><th>Actions</th></tr>';
    
    quizzes.forEach(quiz => {
        html += `
            <tr>
                <td>${quiz.title || 'No Title'}</td>
                <td>${quiz.subject || '-'}</td>
                <td>${quiz.branch || '-'}</td>
                <td>${quiz.semester || '-'}</td>
                <td>
                    <button onclick="viewQuizDetails(${quiz.id})">View</button>
                    <button onclick="viewQuizAttempts(${quiz.id})">Results</button>
                    <button onclick="viewQuizStats(${quiz.id})">Stats</button>
                    <button onclick="deleteQuiz(${quiz.id})" style="color:red;">Delete</button>
                </td>
            </tr>
        `;
    });
    
    html += '</table>';
    container.innerHTML = html;
}

function filterQuizzes() {
    loadQuizzes();
}

function clearFilters() {
    document.getElementById('branchFilter').value = '';
    document.getElementById('subjectFilter').value = '';
    loadQuizzes();
}

function addQuestion() {
    questionCount++;
    const questionList = document.getElementById('questionList');
    
    const questionDiv = document.createElement('div');
    questionDiv.style.border = '1px solid #ddd';
    questionDiv.style.padding = '10px';
    questionDiv.style.margin = '10px 0';
    questionDiv.id = `question-${questionCount}`;
    
    questionDiv.innerHTML = `
        <h5>Question ${questionCount}</h5>
        <div>
            <label>Question Text: 
                <textarea style="width:100%;" rows="2" id="qText-${questionCount}" required></textarea>
            </label>
        </div>
        <div>
            <label>Option A: <input type="text" id="qA-${questionCount}" required /></label>
            <label>Option B: <input type="text" id="qB-${questionCount}" required /></label>
        </div>
        <div>
            <label>Option C: <input type="text" id="qC-${questionCount}" required /></label>
            <label>Option D: <input type="text" id="qD-${questionCount}" required /></label>
        </div>
        <div>
            <label>Correct Option: 
                <select id="qCorrect-${questionCount}">
                    <option value="A">A</option>
                    <option value="B">B</option>
                    <option value="C">C</option>
                    <option value="D">D</option>
                </select>
            </label>
            <label>Marks: <input type="number" id="qMarks-${questionCount}" value="1" required /></label>
        </div>
        <button onclick="removeQuestion(${questionCount})">Remove</button>
    `;
    
    questionList.appendChild(questionDiv);
}

function removeQuestion(id) {
    const elem = document.getElementById(`question-${id}`);
    if (elem) {
        elem.remove();
        questionCount--;
    }
}

function createQuiz() {
    const teacherId = getTeacherId();
    if (!teacherId) {
        alert("Please login again");
        window.location.href = '../login.html';
        return;
    }
    
    // Basic validation
    const title = document.getElementById('quizTitle').value;
    const subject = document.getElementById('quizSubject').value;
    const branch = document.getElementById('quizBranch').value;
    const semester = document.getElementById('quizSemester').value;
    
    if (!title || !subject || !branch || !semester) {
        alert('Please fill all required fields (Title, Subject, Branch, Semester)');
        return;
    }
    
    // Collect questions
    const questions = [];
    let hasQuestions = false;
    
    for (let i = 1; i <= questionCount; i++) {
        const qText = document.getElementById(`qText-${i}`);
        if (!qText) continue;
        
        const questionText = qText.value;
        const optionA = document.getElementById(`qA-${i}`).value;
        const optionB = document.getElementById(`qB-${i}`).value;
        const optionC = document.getElementById(`qC-${i}`).value;
        const optionD = document.getElementById(`qD-${i}`).value;
        
        if (questionText && optionA && optionB && optionC && optionD) {
            hasQuestions = true;
            const question = {
                questionText: questionText,
                optionA: optionA,
                optionB: optionB,
                optionC: optionC,
                optionD: optionD,
                correctOption: document.getElementById(`qCorrect-${i}`).value,
                marks: parseInt(document.getElementById(`qMarks-${i}`).value) || 1
            };
            questions.push(question);
        }
    }
    
    if (!hasQuestions) {
        alert('Please add at least one question');
        return;
    }
    
    // Prepare quiz data - convert teacherId to number
    const quizData = {
        title: title,
        description: title,
        subject: subject,
        branch: branch,
        semester: parseInt(semester),
        section: document.getElementById('quizSection').value || null,
        durationMinutes: parseInt(document.getElementById('quizDuration').value) || 30,
        totalMarks: questions.reduce((sum, q) => sum + q.marks, 0),
        startTime: document.getElementById('quizStartTime').value + ':00',
        endTime: document.getElementById('quizEndTime').value + ':00',
        createdBy: parseInt(teacherId) // Convert to number
    };
    
    const payload = {
        quiz: quizData,
        questions: questions
    };
    
    console.log('Creating quiz with payload:', payload);
    
    fetch('http://localhost:8080/teacher/quiz', {
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token'),
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => { throw new Error(text) });
        }
        return response.json();
    })
    .then(data => {
        alert('✅ Quiz created successfully!');
        hideCreateQuiz();
        loadQuizzes();
    })
    .catch(error => {
        console.error('Error creating quiz:', error);
        alert('❌ Failed to create quiz: ' + error.message);
    });
}

function viewQuizDetails(quizId) {
    fetch(`http://localhost:8080/teacher/quiz/${quizId}`, {
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        }
    })
    .then(response => response.json())
    .then(data => {
        alert(`📝 Quiz Details:\n\nTitle: ${data.quiz.title}\nSubject: ${data.quiz.subject}\nBranch: ${data.quiz.branch}\nTotal Questions: ${data.totalQuestions}`);
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error loading quiz details');
    });
}

function viewQuizAttempts(quizId) {
    fetch(`http://localhost:8080/teacher/quiz/${quizId}/attempts`, {
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        }
    })
    .then(response => response.json())
    .then(attempts => {
        if (!attempts || attempts.length === 0) {
            alert('No attempts found for this quiz');
            return;
        }
        
        let message = `📊 Quiz Attempts (${attempts.length} total):\n\n`;
        attempts.forEach(attempt => {
            const status = attempt.status === 'SUBMITTED' ? '✅ Submitted' : '⏳ In Progress';
            message += `Student ${attempt.studentId}: ${attempt.score} marks - ${status}\n`;
        });
        
        alert(message);
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error loading attempts');
    });
}

function viewQuizStats(quizId) {
    fetch(`http://localhost:8080/teacher/quiz/${quizId}/stats`, {
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        }
    })
    .then(response => response.json())
    .then(stats => {
        let message = '📈 Quiz Statistics:\n\n';
        message += `Total Attempts: ${stats.totalAttempts || 0}\n`;
        message += `Submitted Attempts: ${stats.submittedAttempts || 0}\n`;
        message += `Average Score: ${stats.averageScore || 0}\n`;
        message += `Highest Score: ${stats.highestScore || 0}\n`;
        message += `Lowest Score: ${stats.lowestScore || 0}\n`;
        message += `Completion Rate: ${stats.completionRate || 0}%\n`;
        
        alert(message);
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error loading statistics');
    });
}

function deleteQuiz(quizId) {
    if (!confirm('⚠️ Are you sure you want to delete this quiz? This action cannot be undone.')) return;
    
    fetch(`http://localhost:8080/teacher/quiz/${quizId}`, {
        method: 'DELETE',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        }
    })
    .then(response => {
        if (response.ok) {
            alert('✅ Quiz deleted successfully!');
            loadQuizzes();
        } else {
            alert('❌ Failed to delete quiz');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('❌ Error deleting quiz: ' + error.message);
    });
}

// Make functions available globally
window.loadQuizzes = loadQuizzes;
window.filterQuizzes = filterQuizzes;
window.clearFilters = clearFilters;
window.addQuestion = addQuestion;
window.removeQuestion = removeQuestion;
window.createQuiz = createQuiz;
window.viewQuizDetails = viewQuizDetails;
window.viewQuizAttempts = viewQuizAttempts;
window.viewQuizStats = viewQuizStats;
window.deleteQuiz = deleteQuiz;
window.debugToken = debugToken;