function showUserDetails(firstName, lastName, email, username, mobile, status) {
    const details = document.getElementById('userDetails');
    details.innerHTML = `
        <h3>User Details</h3>
        <p><strong>Full Name:</strong> ${firstName} ${lastName}</p>
        <p><strong>Email:</strong> ${email}</p>
        <p><strong>Username:</strong> ${username}</p>
        <p><strong>Mobile:</strong> ${mobile}</p>
        <p><strong>Status:</strong> ${status}</p>
    `;
    details.style.display = 'block';
    details.scrollIntoView({ behavior: 'smooth' });
}
