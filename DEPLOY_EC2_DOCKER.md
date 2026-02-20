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
- `EC2_USER_INSTANCE_ID`
- `EC2_ADMIN_INSTANCE_ID`
- `EC2_WORKER_INSTANCE_ID`

## Required EC2 conditions
- Docker installed
- IAM role with ECR pull permissions
- SSM Agent active and instance managed by Systems Manager
- `/etc/oplust` env files exist:
  - `/etc/oplust/api-user.env`
  - `/etc/oplust/api-admin.env`
  - `/etc/oplust/transcoder.env`

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

## Verify
On each EC2:
- `docker ps`
- `docker logs oplust-api-user`
- `docker logs oplust-api-admin`
- `docker logs oplust-transcoder`
