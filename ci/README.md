
# docker 安装及使用

## 1、参考
[docker 入门到实践](https://github.com/yeasy/docker_practice)

[docker store](https://store.docker.com/)

[docker hub](https://hub.docker.com/explore/)


## 2、docker 常用命令

```
docker search gitlab

docker pull docker.io/gitlab/gitlab-ce

docker run -d docker.io/gitlab/gitlab-ce

docker ps

docker images
docker image ls
docker image ls -q        // -q 显示 id, -f 过滤， -a 显示中间层镜像
docker images -f dangling=true    // 显示虚悬镜像
docker images --format "table {{.ID}}\t{{.Respository}}\t{{.Tag}}"

docker image rm image_id        // 可使用id（长/短）镜像名，摘要，
docker container rm container_id // 删除容器

docker images -a // 查看所有镜像

docker stop image_id //停止容器

docker exec -it container_id /bin/bash     // 进入容器，exit退出

``` 


## 3、dockerFile 制作

注意事项：

- `from` 语句前不要有其他任何内容，否则构建镜像报错
- 仓库和tag都为 none 的容器可能是某些镜像的依赖，无法直接删除，除非将目标镜像删除，则自动删除，他们不占用空间。
- 使用workdir指定工作目录后，当前目录即为所指定的目录
- 镜像构建是分层的，若后面使用了前面的结果，考虑使用多阶构建，将后层依赖前面层


