# JHipster-generated Kubernetes configuration

## Preparation

You will need to push your image to a registry. If you have not done so, use the following commands to tag and push the images:

```
$ docker image tag gateway eUGB/gateway
$ docker push eUGB/gateway
$ docker image tag microservicegir eUGB/microservicegir
$ docker push eUGB/microservicegir
$ docker image tag microserviceenseignement eUGB/microserviceenseignement
$ docker push eUGB/microserviceenseignement
$ docker image tag microserviceedt eUGB/microserviceedt
$ docker push eUGB/microserviceedt
$ docker image tag microservicegrh eUGB/microservicegrh
$ docker push eUGB/microservicegrh
$ docker image tag microserviceaua eUGB/microserviceaua
$ docker push eUGB/microserviceaua
$ docker image tag microservicedeliberation eUGB/microservicedeliberation
$ docker push eUGB/microservicedeliberation
$ docker image tag microservicegd eUGB/microservicegd
$ docker push eUGB/microservicegd
$ docker image tag microserviceaclc eUGB/microserviceaclc
$ docker push eUGB/microserviceaclc
$ docker image tag microserviceaide eUGB/microserviceaide
$ docker push eUGB/microserviceaide
$ docker image tag microserviceged eUGB/microserviceged
$ docker push eUGB/microserviceged
```

## Deployment

You can deploy all your apps by running the below bash command:

```
./kubectl-apply.sh -f (default option)  [or] ./kubectl-apply.sh -k (kustomize option) [or] ./kubectl-apply.sh -s (skaffold run)
```

If you want to apply kustomize manifest directly using kubectl, then run

```
kubectl apply -k ./
```

If you want to deploy using skaffold binary, then run

```
skaffold run [or] skaffold deploy
```

## Exploring your services

Use these commands to find your application's IP addresses:

```
$ kubectl get svc gateway -n sn-ugb
```

## Scaling your deployments

You can scale your apps using:

```
kubectl scale deployment <app-name> --replicas <replica-count> -n sn-ugb
```

## Zero-downtime deployments

The default way to update a running app in kubernetes, is to deploy a new image tag to your docker registry and then deploy it using:

```
kubectl set image deployment/<app-name>-app <app-name>=<new-image>  -n sn-ugb
```

Using livenessProbes and readinessProbe allow you to tell Kubernetes about the state of your applications, in order to ensure availability of your services. You will need a minimum of two replicas for every application deployment if you want to have zero-downtime.
This is because the rolling upgrade strategy first stops a running replica in order to place a new. Running only one replica, will cause a short downtime during upgrades.

## Monitoring tools

### Prometheus metrics

Generator is also packaged with [Prometheus operator by CoreOS](https://github.com/coreos/prometheus-operator).

**Hint**: use must build your apps with `prometheus` profile active!

Application metrics can be explored in Prometheus through:

```
kubectl get svc jhipster-prometheus -n sn-ugb
```

Also the visualisation can be explored in Grafana which is pre-configured with a dashboard view. You can find the service details by running:

```
kubectl get svc jhipster-grafana -n sn-ugb
```

- If you have chosen _Ingress_, then you should be able to access Grafana using the given ingress domain.
- If you have chosen _NodePort_, then point your browser to an IP of any of your nodes and use the node port described in the output.
- If you have chosen _LoadBalancer_, then use the IaaS provided load balancer IP

## Troubleshooting

> my app doesn't get pulled, because of 'imagePullBackof'

Check the docker registry your Kubernetes cluster is accessing. If you are using a private registry, you should add it to your namespace by `kubectl create secret docker-registry` (check the [docs](https://kubernetes.io/docs/tasks/configure-pod-container/pull-image-private-registry/) for more info)

> my applications are stopped, before they can boot up

This can occur if your cluster has low resource (e.g. Minikube). Increase the `initialDelaySeconds` value of livenessProbe of your deployments

> my applications are starting very slow, despite I have a cluster with many resources

The default setting are optimized for middle-scale clusters. You are free to increase the JAVA_OPTS environment variable, and resource requests and limits to improve the performance. Be careful!
