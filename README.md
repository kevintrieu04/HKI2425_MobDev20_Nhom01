# Hướng dẫn sử dụng dự án CinemaApp

## Tổng quan
- **Backend**: python, flask
- **Frontend**: kotlin, native android

## Yêu cầu cài đặt

### 1. Yêu cầu hệ thống
- Python (phiên bản >= 3.11)
- Android Studio
  
## Hướng dẫn cài đặt và chạy dự án

### 1. Cài đặt Backend
   ```bash
   cd backend
   ```
   ```bash
   pip install -r requirement.txt
   ```
   ```bash
   python app.py
   ```

### 2. Cài đặt và chạy ứng dụng Android

## Cấu trúc dự án

### Backend
```
backend/
├── app.py                     
├── config.py                  # File cấu hình
├── requirement.txt            
├── routes/                    # route các chức năng của api
│   ├── ads_routes.py
│   ├── movie_routes.py
│   ├── search_routes.py
├── utils/
│   └── tmdb_helper.py         # quy định thể loại film
├── ads.json                   # Dữ liệu về quảng cáo
├── movie.json                 
```

### Frontend (Android)
```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/cinemaapp/
│   │   │   ├── models/          
│   │   │   ├── network/         # Quản lý API
│   │   │   ├── ui/              
│   │   │   ├── viewmodels/      
│   │   └── res/                 
├── build.gradle.kts            
├── google-services.json        
```



