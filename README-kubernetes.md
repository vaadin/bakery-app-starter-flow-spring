# Deploy on local kubernetes cluster

## Create a dedicated namespace on local cluster

Type `kubectl create namespace bakery` to create the `bakery` namespace.
If you want to use a different namespace remember to modify
the `jkube.namespace` property in `local-cluster` profile in the project POM
file.

## Build the application for production with postgresql for persistence.

Following command build the application in production mode with postgresql JDBC
driver addition.
It also builds the docker image tagged
as `demo/bakery-app-starter-flow-spring:latest`

`mvn -Pproduction,local-cluster,pgsql package k8s:build`

## Kind only: load the docker image

If using `kind`, local docker image must be loaded into the cluster.

`kind load docker-image demo/bakery-app-starter-flow-spring`

## Deploy the application

To create and apply kubernetes manifest type

`mvn -Pproduction,local-cluster,pgsql k8s:resource k8s:apply`

All resources should be deployed on the cluster and the application should be
accessible through the load balancer.

Type `kubectl -n bakery get svc bakery-app-starter-flow-spring-service` to check
the ip address to use to access the application.

```
 
NAME                                     TYPE           CLUSTER-IP      EXTERNAL-IP      PORT(S)          AGE
bakery-app-starter-flow-spring-service   LoadBalancer   10.96.248.241   172.25.255.204   8000:32508/TCP   25m
```

With the above configuration application should be accessible
at http://172.25.255.204:8000.

## Check that nodes has joined the Hazelcast cluster

Get the list of pods with `kubectl -n bakery get pods`

```
NAME                                              READY   STATUS    RESTARTS   AGE
bakery-app-starter-flow-spring-6d77d87c5c-8ghqx   1/1     Running   0          28m
bakery-app-starter-flow-spring-6d77d87c5c-ncss9   1/1     Running   0          28m
bakery-app-starter-flow-spring-6d77d87c5c-z2t7b   1/1     Running   0          26m
bakery-app-starter-flow-spring-6d77d87c5c-z9lm2   1/1     Running   0          28m
bakery-database-657b6dd9d4-qq8jt                  1/1     Running   0          28m
```

Check logs of any of the pods by typing `kubectl -n backery logs PODNAME`,
where `PODNAME` is one listed from previous command (
e.g. `bakery-app-starter-flow-spring-6d77d87c5c-ncss9`). Scroll up to until you
can see Hazelcast initialization.

```
2022-11-02 15:58:26.717  INFO 1 --- [           main] com.hazelcast.system.logo                : [10.244.0.81]:5701 [dev] [5.1.1] 
	+       +  o    o     o     o---o o----o o      o---o     o     o----o o--o--o
	+ +   + +  |    |    / \       /  |      |     /         / \    |         |   
	+ + + + +  o----o   o   o     o   o----o |    o         o   o   o----o    |   
	+ +   + +  |    |  /     \   /    |      |     \       /     \       |    |   
	+       +  o    o o       o o---o o----o o----o o---o o       o o----o    o   
2022-11-02 15:58:26.718  INFO 1 --- [           main] com.hazelcast.system                     : [10.244.0.81]:5701 [dev] [5.1.1] Copyright (c) 2008-2022, Hazelcast, Inc. All Rights Reserved.
2022-11-02 15:58:26.718  INFO 1 --- [           main] com.hazelcast.system                     : [10.244.0.81]:5701 [dev] [5.1.1] Hazelcast Platform 5.1.1 (20220317 - 5b5fa10) starting at [10.244.0.81]:5701
2022-11-02 15:58:26.718  INFO 1 --- [           main] com.hazelcast.system                     : [10.244.0.81]:5701 [dev] [5.1.1] Cluster name: dev
2022-11-02 15:58:26.718  INFO 1 --- [           main] com.hazelcast.system                     : [10.244.0.81]:5701 [dev] [5.1.1] Integrity Checker is disabled. Fail-fast on corrupted executables will not be performed.
To enable integrity checker do one of the following: 
  - Change member config using Java API: config.setIntegrityCheckerEnabled(true);
  - Change XML/YAML configuration property: Set hazelcast.integrity-checker.enabled to true
  - Add system property: -Dhz.integritychecker.enabled=true (for Hazelcast embedded, works only when loading config via Config.load)
  - Add environment variable: HZ_INTEGRITYCHECKER_ENABLED=true (recommended when running container image. For Hazelcast embedded, works only when loading config via Config.load)
2022-11-02 15:58:26.722  INFO 1 --- [           main] com.hazelcast.system                     : [10.244.0.81]:5701 [dev] [5.1.1] The Jet engine is disabled.
To enable the Jet engine on the members, do one of the following:
  - Change member config using Java API: config.getJetConfig().setEnabled(true)
  - Change XML/YAML configuration property: Set hazelcast.jet.enabled to true
  - Add system property: -Dhz.jet.enabled=true (for Hazelcast embedded, works only when loading config via Config.load)
  - Add environment variable: HZ_JET_ENABLED=true (recommended when running container image. For Hazelcast embedded, works only when loading config via Config.load)
2022-11-02 15:58:27.417  INFO 1 --- [           main] c.h.s.d.integration.DiscoveryService     : [10.244.0.81]:5701 [dev] [5.1.1] Kubernetes Discovery properties: { service-dns: null, service-dns-timeout: 5, service-name: azure-kit-hazelcast-service, service-port: 0, service-label: null, service-label-value: true, namespace: bakery, pod-label: null, pod-label-value: null, resolve-not-ready-addresses: true, expose-externally-mode: AUTO, use-node-name-as-external-address: false, service-per-pod-label: null, service-per-pod-label-value: null, kubernetes-api-retries: 3, kubernetes-master: https://kubernetes.default.svc}
2022-11-02 15:58:27.609  INFO 1 --- [           main] c.h.s.d.integration.DiscoveryService     : [10.244.0.81]:5701 [dev] [5.1.1] Kubernetes Discovery activated with mode: KUBERNETES_API
2022-11-02 15:58:27.610  INFO 1 --- [           main] com.hazelcast.system.security            : [10.244.0.81]:5701 [dev] [5.1.1] Enable DEBUG/FINE log level for log category com.hazelcast.system.security  or use -Dhazelcast.security.recommendations system property to see ? security recommendations and the status of current config.
2022-11-02 15:58:27.732  INFO 1 --- [           main] com.hazelcast.instance.impl.Node         : [10.244.0.81]:5701 [dev] [5.1.1] Using Discovery SPI
2022-11-02 15:58:27.738  WARN 1 --- [           main] com.hazelcast.cp.CPSubsystem             : [10.244.0.81]:5701 [dev] [5.1.1] CP Subsystem is not enabled. CP data structures will operate in UNSAFE mode! Please note that UNSAFE mode will not provide strong consistency guarantees.
2022-11-02 15:58:28.144  INFO 1 --- [           main] c.h.internal.diagnostics.Diagnostics     : [10.244.0.81]:5701 [dev] [5.1.1] Diagnostics disabled. To enable add -Dhazelcast.diagnostics.enabled=true to the JVM arguments.
2022-11-02 15:58:28.151  INFO 1 --- [           main] com.hazelcast.core.LifecycleService      : [10.244.0.81]:5701 [dev] [5.1.1] [10.244.0.81]:5701 is STARTING
2022-11-02 15:58:28.238  INFO 1 --- [           main] c.h.s.d.integration.DiscoveryService     : [10.244.0.81]:5701 [dev] [5.1.1] Cannot fetch the current zone, ZONE_AWARE feature is disabled
2022-11-02 15:58:28.264  INFO 1 --- [           main] c.h.s.d.integration.DiscoveryService     : [10.244.0.81]:5701 [dev] [5.1.1] Kubernetes plugin discovered node name: kind-control-plane
2022-11-02 15:58:28.312  WARN 1 --- [           main] c.hazelcast.kubernetes.KubernetesClient  : Cannot fetch public IPs of Hazelcast Member PODs, you won't be able to use Hazelcast Smart Client from outside of the Kubernetes network
2022-11-02 15:58:28.360  INFO 1 --- [.IO.thread-in-0] c.h.i.server.tcp.TcpServerConnection     : [10.244.0.81]:5701 [dev] [5.1.1] Initialized new cluster connection between /10.244.0.81:35771 and /10.244.0.75:5701
2022-11-02 15:58:28.360  INFO 1 --- [.IO.thread-in-2] c.h.i.server.tcp.TcpServerConnection     : [10.244.0.81]:5701 [dev] [5.1.1] Initialized new cluster connection between /10.244.0.81:48665 and /10.244.0.76:5701
2022-11-02 15:58:28.360  INFO 1 --- [.IO.thread-in-1] c.h.i.server.tcp.TcpServerConnection     : [10.244.0.81]:5701 [dev] [5.1.1] Initialized new cluster connection between /10.244.0.81:60763 and /10.244.0.78:5701
2022-11-02 15:58:33.479  INFO 1 --- [ration.thread-0] c.h.internal.cluster.ClusterService      : [10.244.0.81]:5701 [dev] [5.1.1] 

Members {size:4, ver:6} [
	Member [10.244.0.75]:5701 - 11e6d752-390e-4f90-9de9-547d251dceae
	Member [10.244.0.76]:5701 - 44292811-e669-4028-9c33-e9a4eac29ed4
	Member [10.244.0.78]:5701 - 73798500-eb04-40b9-bf71-e0037b328519
	Member [10.244.0.81]:5701 - 18de754e-56c2-4353-a0d5-81284e7900d0 this
]
```

Verify that `service-name` and `namespace` in the kubernetes configuration are
correct

```
2022-11-02 15:58:27.417  INFO 1 --- [           main] c.h.s.d.integration.DiscoveryService     : [10.244.0.81]:5701 [dev] [5.1.1] Kubernetes Discovery properties: { service-dns: null, service-dns-timeout: 5, service-name: azure-kit-hazelcast-service, service-port: 0, service-label: null, service-label-value: true, namespace: bakery, pod-label: null, pod-label-value: null, resolve-not-ready-addresses: true, expose-externally-mode: AUTO, use-node-name-as-external-address: false, service-per-pod-label: null, service-per-pod-label-value: null, kubernetes-api-retries: 3, kubernetes-master: https://kubernetes.default.svc}
```

Check that more than one node has joined the cluster

```
Members {size:4, ver:6} [
	Member [10.244.0.75]:5701 - 11e6d752-390e-4f90-9de9-547d251dceae
	Member [10.244.0.76]:5701 - 44292811-e669-4028-9c33-e9a4eac29ed4
	Member [10.244.0.78]:5701 - 73798500-eb04-40b9-bf71-e0037b328519
	Member [10.244.0.81]:5701 - 18de754e-56c2-4353-a0d5-81284e7900d0 this
]
```

## Test scaling down

Open the application in the browser and check what pod is serving it by pressing
the `Host Info` button on the Nav Bar.
A notification should tell you the hostname and the ip address.

Navigate the application, for example Users view, add a user and filter the list
to see only several records.

Kill the pod serving the application by
typing `kubectl -n bakery delete pod PODNAME`.

Change the filter, an offline notification may appear or just a loading
indicator on the top of the page, but you should be able to work on the
application without page reloads or being logged out.
