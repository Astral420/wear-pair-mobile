// lib/services/auth_service.dart
class AuthService {
  static const _demoUser = {
    'username': 'testuser',
    'email': 'test@example.com',
    'password': 'password123'
  };

  /// Returns true if credentials match the demo user
  static bool validateLogin(String input, String password) {
    return ((input == _demoUser['username'] || input == _demoUser['email']) &&
        password == _demoUser['password']);
  }
}
