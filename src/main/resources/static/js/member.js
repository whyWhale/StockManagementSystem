var memberApi = {
    init: function () {
        var _this = this;
        $('#btn-save').on('click', function () {
            _this.save();
        });
    },
    save: function () {
        var data = {
            username: $('#username').val(),
            password: $('#password').val(),
            name: $('#name').val(),
            street: $('#street').val(),
            city: $('#city').val(),
            zipcode: $('#zipcode').val(),
        };
        $.ajax({
            type: 'post',
            url: '/api/member',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert("회원가입이 완료되엇습니다.");
            window.location.href = '/member/login';
        }).fail(function (error) {
            alert(error.responseText);
        });
    },
    login: function () {
        var data = {
            username: $('#username').val(),
            password: $('#password').val(),
        };
        $.ajax({
            type: 'post',
            url: '/api/member',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert("login success")
            location.href = '/';
        }).fail(function (error) {
            alert(error.responseText);
        });
    }
}
memberApi.init();