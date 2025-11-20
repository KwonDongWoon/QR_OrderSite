/** ✅ 테이블 번호 가져오기 (URL 파라미터 적용) */
function getTableNo() {
    const params = new URLSearchParams(location.search);
    return params.get("tableNo") || "1"; // 기본값 1
}

const tableNo = getTableNo();
const CART_KEY = `cart_${tableNo}`;  // ✅ 테이블 번호별 분리 저장

let fullMenuData = []; // 전체 메뉴 데이터 저장용

$(document).ready(function () {

    /** ✅ 1. AJAX로 메뉴 목록 불러오기 */
    $.ajax({
        url: "/menu/list/data",
        type: "GET",
        success: function (data) {
            fullMenuData = data;
            renderMenuList(data);
        },
        error: function () {
            alert("메뉴 정보를 불러오는 데 실패했습니다.");
        }
    });

    /** ✅ 2. 메뉴 출력 함수 */
    function renderMenuList(menuList) {
        $("#menu-container").empty();
        menuList.forEach(menu => {
            $("#menu-container").append(`
                <div class="menu-card" data-id="${menu.menuNo}">
                    <img src="${menu.imageUrl}" alt="${menu.menuName}">
                    <h3 class="menu-name">${menu.menuName}</h3>
                    <p class="menu-price">${menu.price}원</p>

                    <div class="quantity-box">
                        <button class="btn-qty minus">-</button>
                        <input type="text" class="qty-input" value="1" readonly>
                        <button class="btn-qty plus">+</button>
                    </div>

                    <button class="btn-add-cart">담기</button>
                </div>
            `);
        });
    }

    /** ✅ 3. 카테고리 필터 */
    $(document).on("click", ".btn-filter", function () {
        $(".btn-filter").removeClass("active");
        $(this).addClass("active");

        const filter = $(this).data("filter");
        if (filter === "all") {
            renderMenuList(fullMenuData);
        } else {
            const filtered = fullMenuData.filter(menu => menu.categoryName === filter);
            renderMenuList(filtered);
        }
    });

    /** ✅ 4. 수량 + / - 버튼 */
    $(document).on("click", ".btn-qty", function () {
        const input = $(this).siblings(".qty-input");
        let current = parseInt(input.val());
        if ($(this).hasClass("plus")) {
            input.val(current + 1);
        } else if ($(this).hasClass("minus") && current > 1) {
            input.val(current - 1);
        }
    });

    /** ✅ 5. 담기 버튼 → LocalStorage 저장 */
    $(document).on("click", ".btn-add-cart", function () {
        const menuCard = $(this).closest(".menu-card");
        const menuNo = menuCard.data("id");
        const name = menuCard.find("h3").text();
        const price = parseInt(menuCard.find("p").text().replace(/[^0-9]/g, ""));
        const quantity = parseInt(menuCard.find(".qty-input").val());

        let cart = JSON.parse(localStorage.getItem(CART_KEY)) || [];
        const existingItem = cart.find(item => item.menuNo === menuNo);

        if (existingItem) {
            existingItem.quantity += quantity;
        } else {
            cart.push({ menuNo, name, price, quantity });
        }

        localStorage.setItem(CART_KEY, JSON.stringify(cart));
        alert(`${name} ${quantity}개 담겼습니다!`);
    });

    /** ✅ 6. 장바구니 열기 */
    $(document).on("click", "#btn-cart", function () {
        const cart = JSON.parse(localStorage.getItem(CART_KEY)) || [];
        renderCart(cart);
        $("#cart-modal").removeClass("hidden");
    });

    /** ✅ 7. 장바구니 닫기 + 메뉴 초기화 */
    $(document).on("click", "#btn-close-cart", function () {
        $("#cart-modal").addClass("hidden");
        $(".btn-filter").removeClass("active");
        $(".btn-filter[data-filter='all']").addClass("active");
        renderMenuList(fullMenuData);
        window.scrollTo({ top: 0, behavior: "smooth" });
    });

    /** ✅ 8. 주문 버튼 → 서버 전송 */
    $(document).on("click", "#btn-order", function () {
        const cart = JSON.parse(localStorage.getItem(CART_KEY)) || [];

        if (cart.length === 0) {
            alert("장바구니가 비어 있습니다.");
            return;
        }

        const orderData = {
            tableNo: parseInt(tableNo),
            peopleCount: 2,
            orderItems: cart.map(item => ({
                menuNo: item.menuNo,
                quantity: item.quantity,
                memo: ""
            }))
        };

        $.ajax({
            url: "/order",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(orderData),
            success: function () {
                alert("✅ 주문이 완료되었습니다!");
                localStorage.removeItem(CART_KEY);
                location.href = `/menu/list?tableNo=${tableNo}`;
            },
            error: function () {
                alert("❌ 주문 처리 중 오류가 발생했습니다.");
            }
        });
    });
});

/** ✅ 장바구니 렌더링 */
function renderCart(cart) {
    let total = 0;
    $("#cart-body").empty();

    cart.forEach((item, index) => {
        $("#cart-body").append(`
            <tr>
                <td>${item.name}</td>
                <td>
                    <button onclick="changeQty(${index}, -1)">-</button>
                    ${item.quantity}
                    <button onclick="changeQty(${index}, 1)">+</button>
                </td>
                <td>${item.price * item.quantity}원</td>
                <td><button onclick="removeItem(${index})">X</button></td>
            </tr>
        `);
        total += item.price * item.quantity;
    });

    $("#cart-total").text(`총 금액: ${total}원`); // ✅ id 수정
}

/** ✅ 항목 삭제 */
function removeItem(index) {
    let cart = JSON.parse(localStorage.getItem(CART_KEY)) || [];
    cart.splice(index, 1);
    localStorage.setItem(CART_KEY, JSON.stringify(cart));
    $("#btn-cart").trigger("click");
}

/** ✅ 수량 변경 */
function changeQty(index, diff) {
    let cart = JSON.parse(localStorage.getItem(CART_KEY)) || [];
    cart[index].quantity += diff;
    if (cart[index].quantity <= 0) return;
    localStorage.setItem(CART_KEY, JSON.stringify(cart));
    $("#btn-cart").trigger("click");
}