# Git branch strategy

## [git flow](http://datasift.github.io/gitflow/IntroducingGitFlow.html)

### branch strategy of this template

| branch | description                                                                                                                                                                                          |
| --- |------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| develop | <ul><li>default branch</li><li>build preview image</li><li>deploy preview</li></ul>                                                                                                                  |
| feature | <ul><li>branch off from develop</li><li>development feature</li><li>check only test</li><li>merge to develop</li></ul>                                                                               |
| release | <ul><li>branch off from develop</li><li>build staging image</li><li>deploy staging</li><li>check from QA</li></ul>                                                                                   |
| main | <ul><li>merge from release</li><li>build production image</li><li>deploy production</li></ul>                                                                                                        |
| bugfix | <ul><li>branch off from develop</li><li>for fix if you found bug in preview</li><li>merge to develop</li></ul>                                                                                       |
| hotfix | <ul><li>branch off from main</li><li>for fix right now</li><ul><li>If service failure in production</li><li>If you found critical bug in production</li></ul><li>merge to main and develop</li></ul> |

# Use service

## DockerHub

- Repository for build image
- It doesn’t need if you use another repository(e.g. AWS ECR).

## Coveralls and Codecov

- Coverage report UI service
- I recommend using coveralls If your team is big.
- I recommend using codecov if your team fewer than 5 or you can pay.

## AWS

- For deploy project
- This template use EKS.

# Add secret for github action

| secret | description |
| --- | --- |
| DOCKERHUB_USERNAME | Your docker hub username |
| DOCKERHUB_TOKEN | Your docker hub access token |
| COVERALLS_REPO_TOKEN | Coveralls repository token of your project repository |
| CODECOV_TOKEN | Codecov token of your project repository |
| AWS_ACCESS_KEY_ID | AWS Access key with permission to the EKS cluster |
| AWS_SECRET_ACCESS_KEY | AWS Secret key corresponding access key |
