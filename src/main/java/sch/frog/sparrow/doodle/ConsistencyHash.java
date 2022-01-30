package sch.frog.sparrow.doodle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 一致性hash算法简单实现
 */
public class ConsistencyHash {

    private static final Logger logger = LoggerFactory.getLogger(ConsistencyHash.class);

    public static void main(String[] args){
        // 初始化数据
        Random r = new Random();
        RepositoryFrontend repository = new RepositoryFrontend();
        String testKey = null;
        for(int i = 0; i < 1000; i++){
            String val = UUID.randomUUID().toString().replaceAll("-", "");
            repository.put(val, String.valueOf(r.nextInt(10000)));
            testKey = val;
        }

        // 查看数据存储分布情况
        Collection<Node> nodes = repository.nodes.values();
        HashSet<String> nodeSet = new HashSet<>();
        for(Node node : nodes){
            if(nodeSet.add(node.address)){
                logger.info("node : {} has key count : {}", node.address, node.kv.size());
            }
        }

        // 查询
        String val = repository.get(testKey);
        logger.info("test query get k : {}, v : {}", testKey, val);
    }

    private static class RepositoryFrontend {

        // 每个真实节点对应的虚拟节点数量
        private static final int visual_node_count = 5;

        TreeMap<Integer, Node> nodes = new TreeMap<>();

        public RepositoryFrontend(){
            addNode(new Node("192.168.1.235"));
            addNode(new Node("192.168.1.120"));
        }

        /**
         * 增加存储节点
         */
        private void addNode(Node node){
            /*
             * 增加虚拟节点, 防止数据倾斜
             * 如果Objects.hash得到的值出现hash冲突怎么办? 为了突出主要逻辑, 这里暂时不考虑
             */
            for(int i = 0; i < visual_node_count; i++){
                nodes.put(Objects.hash(node.address + UUID.randomUUID().toString()), node);
            }
        }

        // 存储
        public void put(String key, String value){
            int keyHash = Objects.hash(key);
            Node node = this.searchNode(keyHash);
            node.kv.put(key, value);
        }

        // 查询
        public String get(String key){
            int keyHash = Objects.hash(key);
            Node node = this.searchNode(keyHash);
            return node.kv.get(key);
        }

        /**
         * 定位到存储的节点
         */
        private Node searchNode(int keyHash){
            if(!nodes.containsKey(keyHash)){ // key的hash值不是正好落在节点上
                // 获取一个子集, 其所有key大于等于keyHash的值
                SortedMap<Integer, Node> tailMap = nodes.tailMap(keyHash);
                if(tailMap.isEmpty()){
                    keyHash = nodes.firstKey();
                }else{
                    keyHash = tailMap.firstKey();
                }
            }
            return nodes.get(keyHash);
        }

    }

    private static class Node{
        HashMap<String, String> kv = new HashMap<>();
        final String address;
        public Node(String address){
            this.address = address;
        }
    }

}
