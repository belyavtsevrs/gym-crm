Feature: Training management
  As a gym admin
  I want to create trainings via API
  So that I can register trainings for trainees

  Scenario: Successfully create a training
    Given I have a valid training request
    When I send a POST request to "/api/trainings/create-training"
    Then the response status should be 200

  Scenario: Fail to create a training
    Given I have an invalid training request
    When I send a POST request to "/api/trainings/create-training"
    Then the response status should be 400
