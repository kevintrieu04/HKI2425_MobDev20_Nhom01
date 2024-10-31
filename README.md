# Outline bài tập lớn.

## I. Giới thiệu chung
File readme này sẽ nói tổng quan về dàn bài và các việc cần hoàn thiện để bọn mình bắt kịp tiến độ
( Nhớ xóa cái này đi trước khi nộp cho thầy nhé :> )

## II. Về dàn bài

Chúng ta sẽ thiết kế ứng dụng theo mô hình MVVM (Model - View - View Model. Hình bên dưới). Trong thư mục src cũng đã chia ra thành các thành phần kiểu này

![image](https://github.com/user-attachments/assets/69afa219-bf89-41f1-a8a4-adbc69ad084a)


Từ sau nếu có thêm tính năng mới thì hãy **để file vào đúng thư mục**. Ví dụ: các file composable để vào thư mục ui, View Model để vào thư mục viewmodels và các data thì để vào model, các file truy cập database hoặc mạng như Room với Retrofit thì để vào thư mục network và database,....


UI lấy mã nguồn từ vid này: https://www.youtube.com/watch?v=2oxFuX6m-qQ. Project gốc sử dụng material design 2 nên là tôi đã chuyển hết lên 3. Tuy nhiên có vài giao diện không thể chuyển hết lên được, nên mình sẽ chỉnh phần này sau

Nhìn chung thì app đã có: Trang chủ, hiện list phim đang chiếu, xem thông tin phim, giao diện chọn ghế.

List phim lấy từ file json tại nguồn này: https://drive.google.com/file/d/135kmAWeGUhTEzvt4V8mdC0CE2sHoKkQU/view?usp=sharing. Anh em tải xuống và bổ sung thêm phim cho mình. Bổ sung xong có thể gửi cho mình hoặc tự upload lên drive rồi chỉnh đường dẫn trong code theo mình hướng dẫn

List quảng cáo (cái thanh cuộn đầu app): https://drive.google.com/file/d/1E7NXafW7JsjKSPRXGK5-8tEpZQOPrsdF/view?usp=sharing


## III. Việc cần làm

Đây là những việc cần làm của nhóm. Ưu tiên từ cao xuống thấp
1. Thiết kế CSDL người dùng
2. Tạo tính năng login, ghi nhận thông tin người dùng vào CSDL
3. Tạo tính năng ghi, xem, xóa bình luận.
4. Tạo filter phân biệt bình luận tốt xấu (theo đề nghị của thầy)
5. Hoàn thiện tính năng đặt vé (Không cần tích hợp thanh toán)
6. Tạo test case
7. Bổ sung dữ liệu phim
8. Làm đẹp giao diện

## IV. Lưu ý

Trước khi bắt đầu làm thì anh em **Đọc kỹ Unit 4 5 6** vì các unit này rất quan trọng nếu muốn tạo các tính năng trên

Phần đặt vé không cần phải tích hợp tính năng thanh toán làm gì. Tức là mình ko cần liên kết ví điện tử hay ngân hàng gì hết, cứ để là ấn nút "thanh toán" thì vé hiện trong tài khoản thôi.

Cơ sở dữ liệu lý tưởng nhất thì nên để online, còn khó quá thì dùng offline cũng đc

