function page(page){
    const itemStatus = document.getElementById("itemStatus").value;
    const itemCategory = document.getElementById("itemCategory").value;
    const itemName = document.getElementById("itemName").value;

    location.href = "/item/manage/?page=" + page + "&itemStatus=" + itemStatus + "&itemCategory=" + itemCategory + "&itemName=" + itemName;
}

function deleteItem(itemId){

    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");

    const url = "/item/delete/" + itemId;

    $.ajax({
        url : url,
        type : "DELETE",
        beforeSend : function(xhr){
            xhr.setRequestHeader(header, token);
        },
        dataType : "json",
        cache : false,
        success : function(result, status){
            alert("상품이 삭제되었습니다.");
            location.href='/item/manage';
        },
        error : function(jqXHR, status, error){
            alert(jqXHR.responseJSON.message);
        }
    });
}