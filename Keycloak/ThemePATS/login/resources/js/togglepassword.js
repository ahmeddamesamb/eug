// document.getElementById('toggle-password-custom').addEventListener('click', function(event) {
//     event.preventDefault();
//     var passwordInput = document.getElementById('password');
//     var toggleButton = document.getElementById('toggle-password-custom');
    
//     if (passwordInput.type === 'password') {
//         passwordInput.type = 'text';
//         toggleButton.textContent = 'Masquer';
//     } else {
//         passwordInput.type = 'password';
//         toggleButton.textContent = 'Afficher';
//     }
// });

document.getElementById('toggle-password-custom').addEventListener('click', function(event) {
    event.preventDefault();
    var passwordInput = document.getElementById('password');
    var toggleButtonIcon = this.querySelector('i');
    
    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        toggleButtonIcon.classList.remove('bi-eye');
        toggleButtonIcon.classList.add('bi-eye-slash');
    } else {
        passwordInput.type = 'password';
        toggleButtonIcon.classList.remove('bi-eye-slash');
        toggleButtonIcon.classList.add('bi-eye');
    }
});