const plusBtn = document.getElementById("plus");
const minusBtn = document.getElementById("minus");
const memberCount = document.getElementById("memberCount");
const memberInput = document.getElementById("member");

let count = 1;

plusBtn.addEventListener("click", () => {
    if (count < 10) count++;
    memberCount.textContent = count;
    memberInput.value = count;
});

minusBtn.addEventListener("click", () => {
    if (count > 1) count--;
    memberCount.textContent = count;
    memberInput.value = count;
});
