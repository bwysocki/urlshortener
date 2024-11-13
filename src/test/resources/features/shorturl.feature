Feature: Short URL Management

  Scenario: Create a short URL successfully
    Given the following URL shorten request:
      """
      {
        "originalUrl": "http://example.com"
      }
      """
    When I send a "POST" request to "/api/v1/urls"
    Then the response status should be 201

  Scenario: Attempt to create a short URL with invalid data
    Given the following URL shorten request:
      """
      {
        "originalUrl": ""
      }
      """
    When I send a "POST" request to "/api/v1/urls"
    Then the response status should be 400
    And the response JSON should contain:
      """
      {"type":"/api/v1/errors/2","title":"Bad request","status":400,"detail":"Invalid request","instance":"/api/v1/urls","errorCode":2,"arguments":{"originalUrl":"Original URL cannot be blank"}}
      """

  Scenario: Attempt to delete a non-existent short URL
    When I send a "DELETE" request to "/api/v1/urls/invalid123"
    Then the response status should be 404
    And the response JSON should contain:
      """
      {"type":"/api/v1/errors/1","title":"Missing entity","status":404,"detail":"The operation could not be completed because one of the core entities was not recognised","instance":"/api/v1/urls/invalid123","errorCode":1,"arguments":{"id":"invalid123","entityType":"ShortUrl"}}
      """
