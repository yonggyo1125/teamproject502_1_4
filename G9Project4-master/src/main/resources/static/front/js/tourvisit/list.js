/* 탭 시작*/
function showContent(tabId) {
    // Hide all tab contents
    var contents = document.querySelectorAll('.tab-content');
    contents.forEach(function(content) {
        content.classList.remove('active');
    });

    // Remove active class from all tab buttons
    var buttons = document.querySelectorAll('.tab-button');
    buttons.forEach(function(button) {
        button.classList.remove('active');
    });

    // Show the clicked tab content
    document.getElementById(tabId).classList.add('active');

    // Add active class to the clicked tab button
    var button = Array.from(buttons).find(button => button.getAttribute('onclick').includes(tabId));
    if (button) {
        button.classList.add('active');
    }
}

// Show the first tab by default
document.addEventListener('DOMContentLoaded', function() {
    showContent('tab1');
});

/* 탭 끝*/