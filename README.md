# [개인프로젝트] 컴퓨터 및 주변장치 쇼핑몰 사이트(컴덕)
## 개발환경
* spring
* spring security
* spring data jpa
* thymeleaf
* java
* javascript
* ajax
* h2 database
* queryDSL
## 테이블 구조
<img src="./table_diagram.png">

## 프로젝트 구조
```
./src/main/java/
└── com/
    └── example/
        └── shop/
            ├── builder/
            │   ├── ErrorMessageBuilderImpl.java
            │   └── ErrorMessageBuilder.java
            ├── configuration/
            │   └── WebMvcConfig.java
            ├── controller/
            │   ├── CartController.java
            │   ├── HomeController.java
            │   ├── ItemController.java
            │   ├── LoginController.java
            │   ├── MemberController.java
            │   ├── OrderController.java
            │   └── ShopController.java
            ├── domain/
            │   ├── baseentity/
            │   │   └── DateBaseEntity.java
            │   ├── CartItem.java
            │   ├── Cart.java
            │   ├── ItemImg.java
            │   ├── Item.java
            │   ├── Member.java
            │   ├── OrderItem.java
            │   ├── Order.java
            │   └── PersistentLogins.java
            ├── Dtos/
            │   ├── cart/
            │   │   ├── CartDto.java
            │   │   ├── CartItemDto.java
            │   │   └── CartOrderDto.java
            │   ├── item/
            │   │   ├── AdminItemFormDto.java
            │   │   ├── ItemFormDto.java
            │   │   ├── ItemImgDto.java
            │   │   ├── ItemSearchDto.java
            │   │   └── UserItemFormDto.java
            │   ├── member/
            │   │   ├── MemberFormDto.java
            │   │   └── PaymentInfoDto.java
            │   └── order/
            │       ├── OrderDto.java
            │       ├── OrderHistDto.java
            │       └── OrderItemDto.java
            ├── enumtype/
            │   ├── DeliveryStatus.java
            │   ├── ItemCategory.java
            │   ├── ItemStatus.java
            │   ├── OrderStatus.java
            │   └── Role.java
            ├── exception/
            │   ├── DeletedItemException.java
            │   ├── OutOfStockException.java
            │   ├── PaymentFailException.java
            │   ├── RefundFailException.java
            │   └── SoldOutException.java
            ├── init/
            │   ├── InitAdmin.java
            │   └── initItem.java
            ├── repository/
            │   ├── cart/
            │   │   ├── CartItemRepository.java
            │   │   └── CartRepository.java
            │   ├── item/
            │   │   ├── ItemRepositoryCustomImpl.java
            │   │   ├── ItemRepositoryCustom.java
            │   │   └── ItemRepository.java
            │   ├── itemimg/
            │   │   └── ItemImgRepository.java
            │   ├── member/
            │   │   └── MemberRepository.java
            │   └── order/
            │       └── OrderRepository.java
            ├── security/
            │   ├── configuration/
            │   │   └── SecurityConfig.java
            │   ├── handler/
            │   │   ├── CustomAuthenticationFailureHandler.java
            │   │   └── CustomAuthenticationSuccessHandler.java
            │   └── member/
            │       └── MemberContext.java
            ├── service/
            │   ├── CartService.java
            │   ├── CustomUserDetailsService.java
            │   ├── FileService.java
            │   ├── ItemImgService.java
            │   ├── ItemService.java
            │   ├── MemberService.java
            │   ├── OrderService.java
            │   └── payment/
            │       ├── IamportService.java
            │       └── PaymentService.java
            └── ShopApplication.java
```

## 주요 기능
### 홈 화면
<img src="./screenshot/홈화면.png">

> 홈 화면 입니다.
### 회원가입
<img src="./screenshot/회원가입.png">

> 회원가입 페이지 입니다.
### 로그인
<img src="./screenshot/로그인.png">

> 로그인 페이지 입니다.
### 상품 등록
<img src="./screenshot/상품등록.png">

> 상품등록 페이지 입니다.<br>
> admin 계정만 접근 가능합니다.<br>
### 상품 관리
<img src="./screenshot/상품관리.png">

> 상품 관리 페이지 입니다.<br>
> admin 계정만 접근 가능합니다.<br>
### 상품 수정
<img src="./screenshot/상품수정.png">

> 상품 수정 페이지 입니다.<br>
> admin 계정만 접근 가능합니다.<br>
> 상품의 이미지를 업로드해 수정 버튼을 누르면 이전의 상품 이미지는 전부 삭제되고 업로드한 이미지가 상품 이미지를 대체합니다.
### shop
<img src="./screenshot/shop.png">

> 쇼핑 페이지 입니다.<br>
### 상품 상세 페이지
<img src="./screenshot/상품보기.png">
<img src="./screenshot/결제.png">

> 상품 상세 페이지 입니다.<br>
### 장바구니
<img src="./screenshot/장바구니.png">
<img src="./screenshot/결제2.png">

> 장바구니 페이지 입니다.<br>
> 로그인한 사용자만이 접근할 수 있습니다.<br>
### 주문내역
<img src="./screenshot/주문내역.png">
<img src="./screenshot/주문취소.png">

> 주문내역 페이지 입니다.<br>
> 로그인한 사용자만이 접근할 수 있습니다.<br>
> 주문 취소 버튼을 통해 주문을 취소하고 환불받을 수 있습니다.