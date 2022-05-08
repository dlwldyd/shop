function addFiles(event){
    const itemImgs = document.getElementById('itemImgs');
    const ImgName = document.getElementById('ImgName');
    if (itemImgs.files.length > 1 && itemImgs.files.length <= 5) {
        ImgName.value = `파일 ${itemImgs.files.length}개`;
    } else if(itemImgs.files.length > 5){
        alert("이미지는 5개 까지 업로드 할 수 있습니다.");
        event.preventDefault();
    } else{
        ImgName.value = itemImgs.value;
    }
}
document.getElementById('itemImgs').addEventListener('change', addFiles);