# Docker EC2 Deployment Guide

## What this deploys
- `apps/api-user` -> `oplust-api-user` image -> User EC2
- `apps/api-admin` -> `oplust-api-admin` image -> Admin EC2
- `apps/transcoder` -> `oplust-transcoder` image -> Worker EC2

Workflow file:
- `.github/workflows/deploy-ec2-docker.yml`

## Required GitHub Secrets
- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`
- `AWS_ACCOUNT_ID`
- `RDS_ENDPOINT` (host only)
- `DB_USERNAME` (shared)
- `DB_PASSWORD` (shared)
- `API_USER_ENV` (service-specific env lines)
- `API_ADMIN_ENV` (service-specific env lines)
- `TRANSCODER_ENV` (service-specific env lines)

## Important
The workflow auto-generates common DB env keys:
- `SPRING_DATASOURCE_URL=jdbc:mysql://<RDS_ENDPOINT>:3306/oplust`
- `SPRING_DATASOURCE_USERNAME=<DB_USERNAME>`
- `SPRING_DATASOURCE_PASSWORD=<DB_PASSWORD>`

Do not include the three DB keys above in `API_USER_ENV`, `API_ADMIN_ENV`, `TRANSCODER_ENV`.

## Secret format example
`API_USER_ENV` value example:

# user-service only vars (optional)

`API_ADMIN_ENV` example:

SPRING_PROFILES_ACTIVE=dev
AWS_REGION=ap-northeast-2
AWS_S3_BUCKET=your-content-bucket
AWS_S3_PRESIGN_EXPIRE_SECONDS=900

`TRANSCODER_ENV` example:

# transcoder-only vars (optional)

## Required EC2 conditions
- Docker installed
- IAM role with ECR pull permissions
- SSM Agent active and instance managed by Systems Manager

## One-time IAM policy for EC2 role (ECR pull)
Attach at least:
- `AmazonEC2ContainerRegistryReadOnly`

## Network prerequisites (private subnet, no NAT)
If EC2 is private without NAT, add VPC interface endpoints:
- `com.amazonaws.ap-northeast-2.ecr.api`
- `com.amazonaws.ap-northeast-2.ecr.dkr`
Also keep S3 gateway endpoint for ECR layer download.

## How to run
1. Open GitHub Actions.
2. Run `Deploy Docker Apps To EC2`.
3. Optional input `image_tag`:
   - empty: deploy current commit SHA
   - set value: deploy that tag
