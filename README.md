# Spring multi module project template

## Description

This template is spring boot project template.

## Git branch strategy

This template use git flow strategy.

[more about](.github/workflows)

## Project structure

- infrastructure
- domain
- service
- application
- util(Optional)

### Infrastructure

About infra.

- Connect Database
- Request third-party api
- Communication another service
- Cache
- Message queue

### Domain

About entities.

- Data entities
- Entity domain logic
- Domain Repository

### Service

About business.

- Use domain interface
    - implementation code will dependency Injection(i.e. JPA Repository)
- Make interface to use service class
    - implement in infrastructure(i.e.  use third party data)

### Application

About api.

- RestController
- Graphql
- (+ more UI View)

### Util(Optional)

About utils.

I don’t make this. Because it is very big module. I think it is project not module.

Maybe I will create util project or use another util project.

- Exception
- (+ utils)
