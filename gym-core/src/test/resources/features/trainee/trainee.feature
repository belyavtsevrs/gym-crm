Feature: Trainee Management
  As a gym application
  I want to manage trainee profiles
  So that I can provide trainee services

  Scenario: Successfully register a trainee
    Given I have trainee registration data with first name "name" and last name "surname"
    When I register the trainee
    Then the registration should be successful with first name "name" and last name "surname"

  Scenario: Update trainee login successfully
    Given there is a trainee with username "rodion.b" and password "old123"
    When I update login for "rodion.b" with old password "old123" and new password "qwerty"
    Then the password should be updated to "qwerty" for username "rodion.b"

  Scenario: Fail to update trainee login with wrong old password
    Given there is no trainee with username "rodion" and password "wrongOldPass"
    When I update login for "rodion" with old password "wrongOldPass" and new password "newSecurePass"
    Then the response should be unauthorized

  Scenario: Get trainee profile successfully
    Given there is a trainee with username "rodion.b"
    When I get trainee profile for "rodion.b"
    Then the profile should contain first name "rodion", last name "b", and address "Address"

  Scenario: Get trainee profile not found
    Given there is no trainee with username "rodion.b"
    When I get trainee profile for "rodion.b"
    Then the response should be not found

  Scenario: Update trainee profile successfully
    Given there is a trainee with username "rodion.b"
    When I update trainee profile for "rodion.b" with data:
      """
      {
        "firstName": "Rodion",
        "lastName": "B",
        "address": "Almaty",
        "isActive": true
      }
      """
    Then the updated profile should contain first name "Rodion", last name "B", address "Almaty", and active true

  Scenario: Get not assigned trainers for trainee
    Given there is a trainee with username "rodion.b"
    When I get not assigned trainers for "rodion.b"
    Then the response should contain available trainers

  Scenario: Update trainee trainers list
    Given there is a trainee with username "rodion.b"
    When I update trainers list for "rodion.b" with trainers: ["trainer1", "trainer2"]
    Then the response should contain updated trainers list

  Scenario: Get trainee training list
    Given there is a trainee with username "rodion.b"
    When I get training list for "rodion.b" with period from "2025-01-01T00:00:00" to "2025-12-31T23:59:59" and trainee name "Rodion"
    Then the response should contain training list

  Scenario: Delete trainee successfully
    Given there is a trainee with username "rodion.b"
    When I delete trainee with username "rodion.b"
    Then the trainee should be deleted

  Scenario: Change trainee status
    Given there is a trainee with username "rodion.b"
    When I change status for "rodion.b" to true
    Then the status should be updated successfully