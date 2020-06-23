# CurrencyRateChanger
Using git on Android

![alt text](https://github.com/TruongGiangVu/CurrencyRateChanger/blob/master/pictures/Screenshot_2.png?raw=true)

## Cấu trúc thư mục
#### -Models: chứa các entity 
#### -Service: gồm các dịch vụ tương tác giữa Models và Activity
#### -Activity: 
    * MainActivity: hiển thị danh sách tiền tệ và tìm kiếm (đã có tìm kiếm và click)
    * CurrencyRateChanger: activity chỉnh để hiển thị chức năng đổi tiền tệ (hiện đã có UI)
    
Tính năng hoàn chỉnh:
- Tìm kiếm tiền tệ theo tên, trả về toàn bộ nếu chuổi tìm kiếm là rỗng
- Chức năng click dể intent activity từ Main sang CurrencyRateChanger
- Button back quay lại trang Main
- Button refresh load lại data từ internet

Tính năng còn thiếu: 
- Tính năng đổi tiền tệ trên activity CurrencyRateChanger 
- Tính năng hoán đổi 2 đơn vị
- Tính năng ghi chép lại lịch sử đổi vào History và hiển thị trên màn hình



P/s: Push code thì push trên nhánh dev nha!
