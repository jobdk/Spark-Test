# ER-Model
![](/Users/john/dev/Studium/Informationssysteme/Informationssysteme-Praktikum-1/Informationssysteme-Praktikum-1/documentation/Small-ER.png)
# Experiments
![](/Users/john/dev/Studium/Informationssysteme/Informationssysteme-Praktikum-1/Informationssysteme-Praktikum-1/documentation/Time-Results.png)

## With primary key
**Line by line**
* Reading:  PT2M7.671678336S
* Mapping:  PT5M2.835406784S
* Database: **PT1H57M25.672997672S**
____________________
**Batch-size 2000**
* Reading:  PT1M25.3264669500:7S
* Mapper:   PT2M24.925795539S
* Database: **PT57M45.090075337S**
____________________

## Without primary key
**Line by line**
* Reading:  PT2M6.677826749S
* Mapper:   PT3M3.932686967S
* Database: PT4M22.576597867S
____________________
 **Batch 30000**
* Reading:  PT16M43.311525128S
* Mapper:   PT2M27.294697208S
* Database: PT1M57.304109706S
____________________


## Batch list size test
**Batch-size: 3000** <br>
PT1M50.620178708S
____________________
**Batch-size 5000** <br>
2:48
____________________
**Batch-size 2000** <br>
PT1M23.155650667S
