Feature: Get all products

  Scenario: get all products
    Given the endpoint for getting all products "api/products/all" with the http GET method is given
    When client want to get all products
    Then response status is 200
