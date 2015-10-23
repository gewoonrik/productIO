sbt clean assembly
cp "target/scala-2.11/productio-server"* "productio-server.jar"
docker build --tag="erwinvaneyk/productio-server" .
docker tag -f erwinvaneyk/productio-server tutum.co/$TUTUM_USER/productio-server
docker push tutum.co/$TUTUM_USER/productio-server
echo "Deployed!"
rm "productio-server.jar"
