const userId = getCurrentUserId();

if (!userId) {
    window.location.href = 'index.html';
}

let currentProjects = [];

async function loadProjects()
{
    const projectsList = document.getElementById('projectsList');
    try
    {
        const page = await apiGet('/projects?page=0&size=50');
        currentProjects = page.content;

        if (currentProjects.length === 0)
        {
            projectsList.innerHTML = '<p class="empty-state">No projects yet. Create one above.</p>';
            return;
        }

        projectsList.innerHTML = currentProjects.map(project => `
            <div class="list-item">
                <div class="list-item-info">
                    <h3>${project.name}</h3>
                    <p>${project.description || 'No description'}</p>
                </div>
                <div class="list-item-actions">
                    <button onclick="viewTasks(${project.id})">View Tasks</button>
                    <button class="btn-danger" onclick="deleteProject(${project.id})">Delete</button>
                </div>
            </div>
        `).join('');
    } catch (err)
    {
        projectsList.innerHTML = `<p class="empty-state">Failed to load projects: ${err.message}</p>`;
    }
}

function viewTasks(projectId)
{
    const project = currentProjects.find(p => p.id === projectId);
    const name = project ? project.name : '';
    window.location.href = `tasks.html?projectId=${projectId}&projectName=${encodeURIComponent(name)}`;
}

async function deleteProject(projectId)
{
    if (!confirm('Delete this project and all its tasks?')) return;
    try
    {
        await apiDelete(`/projects/${projectId}`);
        loadProjects();
    } catch (err)
    {
        alert('Failed to delete project: ' + err.message);
    }
}

document.getElementById('createProjectForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const name = document.getElementById('projectName').value;
    const description = document.getElementById('projectDescription').value;

    try
    {
        await apiPost('/projects', { name, description, owner: { id: parseInt(userId) } });
        document.getElementById('createProjectMessage').textContent = 'Project created!';
        document.getElementById('createProjectMessage').style.color = 'green';
        document.getElementById('createProjectForm').reset();
        loadProjects();
    } catch (err)
    {
        document.getElementById('createProjectMessage').textContent = err.message;
        document.getElementById('createProjectMessage').style.color = 'red';
    }
});

document.getElementById('logoutLink').addEventListener('click', () => {
    logout();
});

loadProjects();