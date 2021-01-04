* Reactive Streams
- 1. Asynchronous  
- 2. Non-Blocking
- 3. Backpressure

*** Publisher <-(subscribe) Subscriber
*** Subscription has been created
*** Publisher (onSubscribe ith subscription) -> Subscriber
*** Subscription <- (request N) Subscriber
*** Publisher -> (onNext) Subscriber
*** Until:
- 1. Publisher sends all the data requested
- 2. Publisher sends all data it has. (onComplete) Subscriber and Subscription will be canceled
- 3. There is an error. (onError) -> subscriber and Subscription will be canceled