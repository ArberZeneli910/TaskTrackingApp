const userId = getCurrentUserId();

if (!userId)
{
    window.location.href = 'index.html';
}

const urlParams = new URLSearchParams(window.location.search);
const projectId = urlParams.get('projectId');
const projectName = urlParams.get('projectName');

if (!projectId)
{
    window.location.href = 'projects.html';
}

document.getElementById('projectTitle').textContent = `Tasks for: ${projectName || 'Project'}`;

let editingTaskId = null;
let currentTasks = [];

async function loadTasks(status = '') {
    const tasksList = document.getElementById('tasksList');
    try
    {
        let url = `/projects/${projectId}/tasks?page=0&size=50`;
        if (status) url += `&status=${status}`;

        const page = await apiGet(url);
        currentTasks = page.content;

        if (currentTasks.length === 0)
        {
            tasksList.innerHTML = '<p class="empty-state">No tasks found.</p>';
            return;
        }

        tasksList.innerHTML = currentTasks.map(task => `
            <div class="list-item">
                <div class="list-item-info">
                    <h3>${task.title}
                        <span class="badge badge-${task.status}">${task.status}</span>
                        <span class="badge badge-${task.priority}">${task.priority}</span>
                    </h3>
                    <p>${task.description || 'No description'}</p>
                    <p>Due: ${task.dueDate || 'No due date'}</p>
                </div>
                <div class="list-item-actions">
                    <div class="list-item-actions">
                        <button onclick="viewComments(${task.id})">Comments</button>
                        <button onclick="editTask(${task.id})">Edit</button>
                        <button class="btn-danger" onclick="deleteTask(${task.id})">Delete</button>
                    </div>
                </div>
            </div>
        `).join('');
    } catch (err)
    {
        tasksList.innerHTML = `<p class="empty-state">Failed to load tasks: ${err.message}</p>`;
    }
}

function editTask(taskId)
{
    const task = currentTasks.find(t => t.id === taskId);
    if (!task) return;

    editingTaskId = task.id;
    document.getElementById('taskTitle').value = task.title;
    document.getElementById('taskDescription').value = task.description || '';
    document.getElementById('taskStatus').value = task.status;
    document.getElementById('taskPriority').value = task.priority;
    document.getElementById('taskDueDate').value = task.dueDate || '';

    document.getElementById('formTitle').textContent = 'Edit Task';
    document.getElementById('taskSubmitBtn').textContent = 'Update Task';
    document.getElementById('cancelEditBtn').style.display = 'inline-block';
}

function cancelEdit()
{
    editingTaskId = null;
    document.getElementById('taskForm').reset();
    document.getElementById('formTitle').textContent = 'Create New Task';
    document.getElementById('taskSubmitBtn').textContent = 'Create Task';
    document.getElementById('cancelEditBtn').style.display = 'none';
}

document.getElementById('cancelEditBtn').addEventListener('click', cancelEdit);

async function deleteTask(taskId)
{
    if (!confirm('Delete this task?')) return;
    try
    {
        await apiDelete(`/tasks/${taskId}`);
        loadTasks(document.getElementById('statusFilter').value);
    } catch (err)
    {
        alert('Failed to delete task: ' + err.message);
    }
}

document.getElementById('statusFilter').addEventListener('change', (e) => {
    loadTasks(e.target.value);
});

document.getElementById('taskForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const taskData =
    {
        title: document.getElementById('taskTitle').value,
        description: document.getElementById('taskDescription').value,
        status: document.getElementById('taskStatus').value,
        priority: document.getElementById('taskPriority').value,
        dueDate: document.getElementById('taskDueDate').value || null,
        assignee: { id: parseInt(userId) }
    };

    try
    {
        if (editingTaskId)
        {
            await apiPut(`/tasks/${editingTaskId}`, taskData);
            document.getElementById('taskFormMessage').textContent = 'Task updated!';
        }
        else
        {
            await apiPost(`/projects/${projectId}/tasks`, taskData);
            document.getElementById('taskFormMessage').textContent = 'Task created!';
        }
        document.getElementById('taskFormMessage').style.color = 'green';
        cancelEdit();
        loadTasks(document.getElementById('statusFilter').value);
    } catch (err)
    {
        document.getElementById('taskFormMessage').textContent = err.message;
        document.getElementById('taskFormMessage').style.color = 'red';
    }
});

let currentCommentTaskId = null;

async function viewComments(taskId) {
    currentCommentTaskId = taskId;
    document.getElementById('commentsSection').style.display = 'block';
    document.getElementById('commentsTitle').textContent = `Comments for Task #${taskId}`;
    await loadComments(taskId);
}

async function loadComments(taskId) {
    const commentsList = document.getElementById('commentsList');
    try {
        const comments = await apiGet(`/tasks/${taskId}/comments`);
        if (comments.length === 0) {
            commentsList.innerHTML = '<p class="empty-state">No comments yet.</p>';
            return;
        }
        commentsList.innerHTML = comments.map(comment => `
            <div class="list-item">
                <div class="list-item-info">
                    <p>${comment.content}</p>
                    <p><small>${comment.author ? comment.author.username : 'Unknown'} — ${new Date(comment.createdAt).toLocaleString()}</small></p>
                </div>
                <div class="list-item-actions">
                    <button class="btn-danger" onclick="deleteComment(${comment.id})">Delete</button>
                </div>
            </div>
        `).join('');
    } catch (err) {
        commentsList.innerHTML = `<p class="empty-state">Failed to load comments: ${err.message}</p>`;
    }
}

async function deleteComment(commentId) {
    if (!confirm('Delete this comment?')) return;
    try {
        await apiDelete(`/comments/${commentId}`);
        loadComments(currentCommentTaskId);
    } catch (err) {
        alert('Failed to delete comment: ' + err.message);
    }
}

document.getElementById('commentForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const content = document.getElementById('commentContent').value;

    try {
        await apiPost(`/tasks/${currentCommentTaskId}/comments`, { content });
        document.getElementById('commentContent').value = '';
        loadComments(currentCommentTaskId);
    } catch (err) {
        alert('Failed to add comment: ' + err.message);
    }
});

document.getElementById('logoutLink').addEventListener('click', () => {
    logout();
});

loadTasks();