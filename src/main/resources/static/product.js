var stomp = null;

function renderItem(productJson) {
    var product = JSON.parse(productJson.body);
    $("#table")
        .append(
            "<tr>" +
                "<td>" + product.title + "</td>" +
                "<td>" + product.price + "</td>" +
                "<td><a href='/products'" + product.id + "/bucket'>Add to bucket</a></td>" +
            "</tr>"
        );
}

function connect() {
    var socket = new SockJS('/socket');
    stomp = Stomp.over(socket);
    stomp.connect({}, function (frame) {
        console.log('Connected ' + frame);
        stomp.subscribe('/topic/products', function (product) {
            renderItem(product);
        });
    });
}

window.onload = function () {
    connect();
}

function sendContent() {
    stomp.send("/app/products", {}, JSON.stringify({
        'title': $("#title").val(),
        'price': $("#price").val(),
    }));
}

$(function () {
    $("form").on('sumbit', function (e) {
        e.preventDefault();
    });
    $("#send").click(function () {
        sendContent();
    });
})