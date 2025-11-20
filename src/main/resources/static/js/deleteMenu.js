document.addEventListener("DOMContentLoaded", function() {
    const deleteButtons = document.querySelectorAll(".btn-delete");

    deleteButtons.forEach(btn => {
        btn.addEventListener("click", function(event) {
            event.preventDefault(); // a태그의 기본 이동 막기

            const menuNo = this.getAttribute("data-id");

            if (confirm("정말 삭제하시겠습니까?")) {
                window.location.href = `/admin/menu/delete/${menuNo}`;
            }
        });
    });
});