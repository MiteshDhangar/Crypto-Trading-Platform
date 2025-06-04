# 🚀 Crypto Trading Platform (Backend) with AI Chatbot

A powerful and modular backend application for a complete crypto trading platform. Designed and built solo using **Java & Spring Boot**, this system integrates real-time market data, secure user operations, trading logic, wallet management, and a Gemini-powered AI chatbot for intelligent user interaction.

---

## 🛠 Tech Stack

- **Backend Framework:** Java, Spring Boot
- **Security:** JWT Authentication, Java Mail, Two-Factor Authentication (2FA)
- **Database:** MySQL
- **API Integrations:**
  - [CoinGecko API](https://www.coingecko.com/en/api): Live coin market data
  - [Gemini API](https://ai.google.dev): Google Gemini-based AI assistant
  - [Razorpay](https://razorpay.com) & [Stripe](https://stripe.com): Mock payment integrations
- **Tools & Others:** Maven, Postman, REST APIs

---

## 🔧 Core Features

### 🔐 Authentication & Security
- User signup and login with **JWT token-based sessions**
- Optional **Two-Factor Authentication (2FA)** for added security

### 📊 Live Market Data (CoinGecko)
- View trending coins
- Search any coin with full market insights
- Price charts and rankings

### ⭐ Watchlist Functionality
- Users can track their favorite coins
- Add/remove easily

### 💰 Wallet & Payments
- Simulate wallet balance
- Add funds using Razorpay/Stripe mock endpoints
- Wallet-to-wallet transfer support

### 🔄 Trading Engine
- Place **BUY/SELL orders**
- Maintain user asset portfolios

### 💳 Withdrawals & Bank System
- Add bank details
- Request fund withdrawals (mock admin approval)

### 🤖 Gemini AI Chatbot
- Chatbot interprets natural prompts like:
  > "What’s the current price of Bitcoin?"  
  > "How is the crypto market today?"

- Performs **function calling** to fetch live crypto stats
- Falls back to generic chat for non-market queries

---

## 📈 Challenges Faced & Learnings

- Integrated diverse APIs (CoinGecko, Gemini, Razorpay, Stripe)
- Engineered fallback logic for Gemini AI when functions aren't triggered
- Built consistent error handling and clear JSON responses
- Designed modular service structure, clean enough for future microservices migration

---

## 🚧 What's Next?

- 🎨 Building a frontend UI using React or Angular
- ☁️ Hosting & deployment on cloud (Render, Railway, or AWS)
- ⚙️ Converting this monolithic app into a **microservices architecture**

---

📽 **Demo Video (Postman + Chatbot in Action):** _Coming soon_  
🔗 **GitHub Source Code:** https://github.com/MiteshDhangar/Crypto-Trading-Platform

---

💬 **Open to Collaborate:**  
Interested in building the frontend or working on deployment? Let’s connect and bring this platform to life!

