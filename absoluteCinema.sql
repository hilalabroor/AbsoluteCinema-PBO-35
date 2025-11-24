-- 1. Buat Database Baru
CREATE DATABASE IF NOT EXISTS db_absolut_cinema;
USE db_absolut_cinema;

-- 2. Buat Tabel USERS (Dengan status langganan)
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- Di tugas kuliah usually plain text oke, tapi best practice di-hash
    subscription_type ENUM('FREE', 'PREMIUM') DEFAULT 'FREE',
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Buat Tabel FILMS (Lengkap dengan path multimedia)
CREATE TABLE films (
    film_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    genre VARCHAR(50), -- Contoh: 'Action', 'Drama'
    rating DECIMAL(3, 1), -- Contoh: 8.5
    release_year INT,
    synopsis TEXT,
    poster_path VARCHAR(255),      -- Nama file gambar (misal: "avengers.png")
    trailer_gif_path VARCHAR(255), -- Nama file GIF (misal: "avengers_preview.gif")
    audio_path VARCHAR(255)        -- Nama file Suara (misal: "avengers_bgm.wav")
);

-- 4. Buat Tabel USER_FILMS (Many-to-Many untuk Watchlist & History)
CREATE TABLE user_films (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    film_id INT,
    list_type ENUM('WATCHLIST', 'HISTORY') NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (film_id) REFERENCES films(film_id) ON DELETE CASCADE
);

-- 5. Masukkan Data Dummy (PENTING: Agar saat run program tidak kosong)

-- Dummy Users
INSERT INTO users (username, password, subscription_type) VALUES 
('si_gratisan', '12345', 'FREE'),
('sultan_premium', 'admin123', 'PREMIUM');

-- Dummy Films (Pastikan nama file ini nanti Anda buat di folder aset)
INSERT INTO films (title, genre, rating, release_year, synopsis, poster_path, trailer_gif_path, audio_path) VALUES 
('The Dark Knight', 'Action', 9.0, 2008, 'Batman menghadapi Joker di Gotham.', 'dark_knight.jpg', 'dark_knight.gif', 'dark_knight.wav'),
('Inception', 'Sci-Fi', 8.8, 2010, 'Pencurian rahasia melalui mimpi.', 'inception.jpg', 'inception.gif', 'inception.wav'),
('Parasite', 'Drama', 8.6, 2019, 'Kesenjangan sosial keluarga Korea.', 'parasite.jpg', 'parasite.gif', 'parasite.wav'),
('Interstellar', 'Sci-Fi', 8.6, 2014, 'Perjalanan melintasi lubang cacing.', 'interstellar.jpg', 'interstellar.gif', 'interstellar.wav'),
('Joker', 'Drama', 8.4, 2019, 'Kisah asal usul penjahat Gotham.', 'joker.jpg', 'joker.gif', 'joker.wav');