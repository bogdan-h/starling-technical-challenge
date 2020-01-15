Feature: Roundup

  Scenario Outline: Should accept and validate minTransactionTimestamp and maxTransactionTimestamp query parameters
    When I invoke the roundup feature on transactions between '<minTransactionTimestamp>' and '<maxTransactionTimestamp>'
    Then The HTTP response status will be <status>
    Examples:
      | minTransactionTimestamp  | maxTransactionTimestamp  | status |
      | 2019-01-01T00:00:00.000Z | 2019-01-02T00:00:00.000Z | 200    |
      | 2019-01-01T00:00:00.000  | 2019-01-02T00:00:00.000Z | 400    |
      | 2019-01-01T00:00:00.000Z | 2019-01-02T00:00:00.000  | 400    |
      | 2019-01-01T00:00:00.000Z | 123                      | 400    |
      | 123                      | 2019-01-02T00:00:00.000Z | 400    |