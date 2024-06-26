#!/bin/bash
# Files are ordered in proper order with needed wait for the dependent custom resource definitions to get initialized.
# Usage: bash kubectl-apply.sh

usage(){
 cat << EOF

 Usage: $0 -f
 Description: To apply k8s manifests using the default \`kubectl apply -f\` command
[OR]
 Usage: $0 -k
 Description: To apply k8s manifests using the kustomize \`kubectl apply -k\` command
[OR]
 Usage: $0 -s
 Description: To apply k8s manifests using the skaffold binary \`skaffold run\` command

EOF
exit 0
}

logSummary() {
    echo ""
        echo "#####################################################"
        echo "Please find the below useful endpoints,"
        echo "JHipster Grafana - http://jhipster-grafana.sn-ugb.ugb.sn"
        echo "#####################################################"
}

default() {
    suffix=k8s
    kubectl apply -f namespace.yml
    kubectl label namespace sn-ugb istio-injection=enabled --overwrite=true
    kubectl apply -f registry-${suffix}/
    kubectl apply -f gateway-${suffix}/
    kubectl apply -f microservicegir-${suffix}/
    kubectl apply -f microserviceenseignement-${suffix}/
    kubectl apply -f microserviceedt-${suffix}/
    kubectl apply -f microservicegrh-${suffix}/
    kubectl apply -f microserviceaua-${suffix}/
    kubectl apply -f microservicedeliberation-${suffix}/
    kubectl apply -f microservicegd-${suffix}/
    kubectl apply -f microserviceaclc-${suffix}/
    kubectl apply -f microserviceaide-${suffix}/
    kubectl apply -f microserviceged-${suffix}/
    kubectl apply -f monitoring-${suffix}/jhipster-prometheus-crd.yml
    until [ $(kubectl get crd prometheuses.monitoring.coreos.com 2>>/dev/null | wc -l) -ge 2 ]; do
        echo "Waiting for the custom resource prometheus operator to get initialised";
        sleep 5;
    done
    kubectl apply -f monitoring-${suffix}/jhipster-prometheus-cr.yml
    kubectl apply -f monitoring-${suffix}/jhipster-grafana.yml
    kubectl apply -f monitoring-${suffix}/jhipster-grafana-dashboard.yml

}

kustomize() {
    kubectl apply -k ./
}

scaffold() {
    // this will build the source and apply the manifests the K8s target. To turn the working directory
    // into a CI/CD space, initilaize it with `skaffold dev`
    skaffold run
}

[[ "$@" =~ ^-[fks]{1}$ ]]  || usage;

while getopts ":fks" opt; do
    case ${opt} in
    f ) echo "Applying default \`kubectl apply -f\`"; default ;;
    k ) echo "Applying kustomize \`kubectl apply -k\`"; kustomize ;;
    s ) echo "Applying using skaffold \`skaffold run\`"; scaffold ;;
    \? | * ) usage ;;
    esac
done

logSummary
