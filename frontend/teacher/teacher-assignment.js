// teacher-assignment.js
function getTeacherId() {
    const token = localStorage.getItem('token');
    if (!token) return null;
    
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        const username = payload.sub;
        // Simple mapping - change based on your IDs
        if (username === 'teacherA') return 1;
        if (username === 'teacherB') return 2;
        return 1; // default
    } catch (error) {
        console.error('Token error:', error);
        return 1;
    }
}

function loadAssignments() {
    const teacherId = getTeacherId();
    
    fetch(`http://localhost:8080/teacher/assignment?teacherId=${teacherId}`, {
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        }
    })
    .then(response => response.json())
    .then(assignments => {
        displayAssignments(assignments);
    })
    .catch(error => {
        console.error('Error:', error);
        document.getElementById('assignmentsContainer').innerHTML = 'Error loading assignments';
    });
}

function displayAssignments(assignments) {
    const container = document.getElementById('assignmentsContainer');
    
    if (!assignments || assignments.length === 0) {
        container.innerHTML = '<p>No assignments found</p>';
        return;
    }
    
    let html = '<table border="1" style="width:100%;">';
    html += '<tr><th>Title</th><th>Subject</th><th>Branch</th><th>Due Date</th><th>Actions</th></tr>';
    
    assignments.forEach(assignment => {
        const dueDate = assignment.dueDate ? new Date(assignment.dueDate).toLocaleDateString() : '-';
        
        html += `
            <tr>
                <td>${assignment.title}</td>
                <td>${assignment.subject || '-'}</td>
                <td>${assignment.branch || '-'}</td>
                <td>${dueDate}</td>
                <td>
                    <button onclick="viewAssignment(${assignment.id})">View</button>
                    <button onclick="viewStats(${assignment.id})">Stats</button>
                    <button onclick="deleteAssignment(${assignment.id})">Delete</button>
                </td>
            </tr>
        `;
    });
    
    html += '</table>';
    container.innerHTML = html;
}

function createAssignment() {
    const teacherId = getTeacherId();
    
    const assignmentData = {
        title: document.getElementById('assignmentTitle').value,
        description: document.getElementById('assignmentDesc').value,
        subject: document.getElementById('assignmentSubject').value,
        branch: document.getElementById('assignmentBranch').value,
        semester: parseInt(document.getElementById('assignmentSemester').value),
        maxMarks: parseInt(document.getElementById('assignmentMaxMarks').value),
        dueDate: document.getElementById('assignmentDueDate').value + ':00',
        attachmentUrl: document.getElementById('assignmentAttachment').value || null,
        createdBy: teacherId
    };
    
    fetch('http://localhost:8080/teacher/assignment', {
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token'),
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(assignmentData)
    })
    .then(response => response.json())
    .then(data => {
        alert('Assignment created!');
        hideCreateAssignment();
        loadAssignments();
    })
    .catch(error => {
        alert('Error: ' + error.message);
    });
}

function viewAssignment(assignmentId) {
    fetch(`http://localhost:8080/teacher/assignment/${assignmentId}`, {
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        }
    })
    .then(response => response.json())
    .then(assignment => {
        alert(`Assignment: ${assignment.title}\nSubject: ${assignment.subject}\nMarks: ${assignment.maxMarks}`);
    })
    .catch(error => console.error('Error:', error));
}

function viewStats(assignmentId) {
    fetch(`http://localhost:8080/teacher/assignment/${assignmentId}/stats`, {
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        }
    })
    .then(response => response.json())
    .then(stats => {
        let message = 'Statistics:\n\n';
        for (const [key, value] of Object.entries(stats)) {
            message += `${key}: ${value}\n`;
        }
        alert(message);
    })
    .catch(error => console.error('Error:', error));
}

function deleteAssignment(assignmentId) {
    if (!confirm('Delete this assignment?')) return;
    
    fetch(`http://localhost:8080/teacher/assignment/${assignmentId}`, {
        method: 'DELETE',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        }
    })
    .then(response => {
        if (response.ok) {
            alert('Deleted!');
            loadAssignments();
        } else {
            alert('Delete failed');
        }
    })
    .catch(error => console.error('Error:', error));
}