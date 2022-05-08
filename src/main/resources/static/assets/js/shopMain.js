function page(page){
    const itemStatus = document.getElementById("itemStatus").value;
    const itemCategory = document.getElementById("itemCategory").value;
    const itemName = document.getElementById("itemName").value;

    location.href = "/shop?page=" + page + "&itemStatus=" + itemStatus + "&itemCategory=" + itemCategory + "&itemName=" + itemName;
}