Feature: Find Product by id

  Scenario:
    Given Product service is up and running
      And Product endpoint "api/products/" with http method GET available
    When client wants to find a product with id 1
    Then response code is 404
