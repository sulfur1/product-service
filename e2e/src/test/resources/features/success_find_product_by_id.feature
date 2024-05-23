Feature: Find Product by id

  Scenario:
    Given Product service is up and running
      And Product endpoint "api/v1/products/" with http method GET available
    When client wants to find a product with id 1
    Then response code is 200
      And found product response body contains:
        | id | summary          | description                 | active|
        | 1  | Spring in Action | This is course about Spring | true  |
