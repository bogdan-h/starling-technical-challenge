Feature: Roundup

  Scenario Outline: Should accept and validate minTransactionTimestamp and maxTransactionTimestamp query parameters
    Given The Accounts API responds with 200
    And The Transaction Feed API responds with 200
    And The Savings Goal API responds with 200
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
    And The Transaction Feed API responds with 200
    And The Savings Goal API responds with 200
    When I invoke the roundup feature on transactions between '2019-01-01T00:00:00.000Z' and '2019-01-02T00:00:00.000Z'
    Then The HTTP response status will be <status>
    And The HTTP error message will be '<message>'
    Examples:
      | accounts_api_status | status | message                                    |
      | 400                 | 500    | Failed to call Accounts API correctly      |
      | 401                 | 500    | Failed to call Accounts API correctly      |
      | 403                 | 500    | Failed to call Accounts API correctly      |
      | 404                 | 500    | Failed to call Accounts API correctly      |
      | 500                 | 502    | Accounts API failed to fulfill the request |

  Scenario: Should handle Accounts API timeout
    Given The Accounts API times out
    And The Transaction Feed API responds with 200
    And The Savings Goal API responds with 200
    When I invoke the roundup feature on transactions between '2019-01-01T00:00:00.000Z' and '2019-01-02T00:00:00.000Z'
    Then The HTTP response status will be 504
    And The HTTP error message will be 'Accounts API timed out'

  Scenario: Should handle Transaction Feed API timeout
    Given The Accounts API responds with 200
    And The Transaction Feed API times out
    And The Savings Goal API responds with 200
    When I invoke the roundup feature on transactions between '2019-01-01T00:00:00.000Z' and '2019-01-02T00:00:00.000Z'
    Then The HTTP response status will be 504
    And The HTTP error message will be 'Transaction Feed API timed out'

  Scenario: Should handle Savings Goal API timeout
    Given The Accounts API responds with 200
    And The Transaction Feed API responds with 200
    And The Savings Goal API times out
    When I invoke the roundup feature on transactions between '2019-01-01T00:00:00.000Z' and '2019-01-02T00:00:00.000Z'
    Then The HTTP response status will be 504
    And The HTTP error message will be 'Savings Goal API timed out'

  Scenario Outline: Should handle Transaction Feed API errors
    Given The Accounts API responds with 200
    And The Transaction Feed API responds with <transaction_feed_api_status>
    And The Savings Goal API responds with 200
    When I invoke the roundup feature on transactions between '2019-01-01T00:00:00.000Z' and '2019-01-02T00:00:00.000Z'
    Then The HTTP response status will be <status>
    And The HTTP error message will be '<message>'
    Examples:
      | transaction_feed_api_status | status | message                                            |
      | 400                         | 500    | Failed to call Transaction Feed API correctly      |
      | 401                         | 500    | Failed to call Transaction Feed API correctly      |
      | 403                         | 500    | Failed to call Transaction Feed API correctly      |
      | 404                         | 500    | Failed to call Transaction Feed API correctly      |
      | 500                         | 502    | Transaction Feed API failed to fulfill the request |

  Scenario Outline: Should handle Savings Goal API errors
    Given The Accounts API responds with 200
    And The Transaction Feed API responds with 200
    And The Savings Goal API responds with <savings_goal_api_status>
    When I invoke the roundup feature on transactions between '2019-01-01T00:00:00.000Z' and '2019-01-02T00:00:00.000Z'
    Then The HTTP response status will be <status>
    And The HTTP error message will be '<message>'
    Examples:
      | savings_goal_api_status | status | message                                        |
      | 400                     | 500    | Failed to call Savings Goal API correctly      |
      | 401                     | 500    | Failed to call Savings Goal API correctly      |
      | 403                     | 500    | Failed to call Savings Goal API correctly      |
      | 404                     | 500    | Failed to call Savings Goal API correctly      |
      | 500                     | 502    | Savings Goal API failed to fulfill the request |

  Scenario: Should call the Starling APIs correctly
    Given The Accounts API responds with 200
    And The Transaction Feed API responds with 200
    And The Savings Goal API responds with 200
    When I invoke the roundup feature on transactions between '2019-01-01T00:00:00.000Z' and '2019-01-02T00:00:00.000Z'
    Then The Accounts API has been called correctly
    And The Transaction Feed API has been called correctly for transactions between '2019-01-01T00:00Z' and '2019-01-02T00:00Z'
    And The Savings Goal API has been called correctly

  Scenario: Should roundup feed items and add to savings account
    Given The Accounts API responds with 200
    And The Transaction Feed API responds with the following feed items
      | amount | currency |
      | 12     | GBP      |
      | 24     | GBP      |
    And The Savings Goal API responds with 200
    When I invoke the roundup feature on transactions between '2019-01-01T00:00:00.000Z' and '2019-01-02T00:00:00.000Z'
    Then The Savings Goal API has been called correctly with amount 164 of currency 'GBP'

  Scenario: Should handle failed transfer into savings account
    Given The Accounts API responds with 200
    And The Transaction Feed API responds with 200
    And The Savings Goal API responds with error message 'You reached your Savings Goal limit'
    When I invoke the roundup feature on transactions between '2019-01-01T00:00:00.000Z' and '2019-01-02T00:00:00.000Z'
    Then The HTTP response status will be 200
    And The HTTP error message will be 'You reached your Savings Goal limit'