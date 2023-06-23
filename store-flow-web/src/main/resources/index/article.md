Article 对象对应的索引

- "number_of_shards" : "1", 设置分片数量
- "number_of_replicas" : "1" 设置副本数量

```json
PUT article_index
{
  "mappings": {
    "properties": {
      "author": {
        "type":"nested",
        "properties" : {
          "age" : {
            "type" : "long"
          },
          "name" : {
            "type" : "text",
            "fields" : {
              "keyword" : {
                "type" : "keyword",
                "ignore_above" : 256
              }
            }
          }
        }
      },
      "context" : {
        "type" : "text",
        "analyzer":"ik_max_word",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      },
      "created" : {
        "type" : "date"
      },
      "id" : {
        "type" : "long"
      },
      "title" : {
        "type" : "text",
        "analyzer":"ik_max_word",
        "fields" : {
          "keyword" : {
            "type" : "keyword",
            "ignore_above" : 256
          }
        }
      }
    }
  },
  "settings": {
    "number_of_shards" : "2",
    "number_of_replicas" : "1"
  }
}
```






