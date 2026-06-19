document.getElementById('registerForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const username = document.getElementById('regUsername').value;
    const email = document.getElementById('regEmail').value;
    const password = document.getElementById('regPassword').value;

    try
    {
        await apiPost('/users', { username, email, password });
        document.getElementById('registerMessage').textContent =
            'Registered! You can now log in below using your username and password.';
        document.getElementById('registerMessage').style.color = 'green';
    } catch (err)
    {
        document.getElementById('registerMessage').textContent = err.message;
        document.getElementById('registerMessage').style.color = 'red';
    }
});

document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const username = document.getElementById('loginUsername').value;
    const password = document.getElementById('loginPassword').value;

    try
    {
        const data = await apiPost('/auth/login', { username, password });
        setAuthToken(data.token);
        setCurrentUserId(data.userId);
        window.location.href = 'projects.html';
    } catch (err)
    {
        document.getElementById('loginMessage').textContent = 'Invalid username or password';
        document.getElementById('loginMessage').style.color = 'red';
    }
});