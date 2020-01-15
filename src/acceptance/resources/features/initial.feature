Feature: Roundup

  Scenario Outline: Should accept and validate minTransactionTimestamp and maxTransactionTimestamp query parameters
    Given The Accounts API responds with 200
    When I invoke the roundup feature on transactions between '<minTransactionTimestamp>' and '<maxTransactionTimestamp>'
    Then The HTTP response status will be <status>
    Examples:
      | minTransactionTimestamp  | maxTransactionTimestamp  | status |
      | 2019-01-01T00:00:00.000Z | 2019-01-02T00:00:00.000Z | 200    |
      | 2019-01-01T00:00:00.000  | 2019-01-02T00:00:00.000Z | 400    |
      | 2019-01-01T00:00:00.000Z | 2019-01-02T00:00:00.000  | 400    |
      | 2019-01-01T00:00:00.000Z | 123                      | 400    |
      | 123                      | 2019-01-02T00:00:00.000Z | 400    |

  Scenario Outline: Should handle Accounts API errors
    Given The Accounts API responds with <accounts_api_status>
    When I invoke the roundup feature on transactions between '2019-01-01T00:00:00.000Z' and '2019-01-02T00:00:00.000Z'
    Then The HTTP response status will be <status>
    Examples:
      | accounts_api_status | status |
      | 200                 | 200    |
      | 400                 | 500    |
      | 401                 | 500    |
      | 403                 | 500    |
      | 404                 | 500    |
      | 500                 | 502    |

  Scenario: Should call the Accounts API correctly
    Given The Accounts API responds with 200
    When I invoke the roundup feature on transactions between '2019-01-01T00:00:00.000Z' and '2019-01-02T00:00:00.000Z'
    Then The Accounts API has been called correctly