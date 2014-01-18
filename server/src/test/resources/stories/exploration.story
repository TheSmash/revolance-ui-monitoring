Narrative:

As a QA I want to detect changes
on the UI of my application so that
I can review the changes when developers
don't know what feature their new code could break

Meta:
@exploration

Scenario: Explore sitemap-ref

Given goto <URL>/applications/declare?withPhantom=true
Given do type sitemap-ref in tag
Given do type <PAGE_REF> in domain
Given do type index.html in page
Given do click on 800x600
Given do click on PhantomJS
When do click on start-exploration
Then await 60 s
Then goto <URL>/applications
Then applications-count==1

Scenario: Explore sitemap-new-content

Given goto <URL>/applications/declare?withPhantom=true
Given do type sitemap-new-content in tag
Given do type <PAGE_NEW_CONTENT> in domain
Given do type index.html in page
Given do click on 800x600
Given do click on PhantomJS
When do click on start-exploration
Then await 60 s
Then goto <URL>/applications
Then applications-count==2

Scenario: Create a review between sitemap-ref and sitemap-new-content

Given goto <URL>/reviews/declare
Given do select sitemap-ref as applicationId
Given do click on no
Given do select sitemap-new-content as referenceApplicationId
Given do type TheSmash@Revolance in reviewer
When do click on create-review
Then reviews-count==1


