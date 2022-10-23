# ER-Model
![](/Users/john/dev/Studium/Informationssysteme/Informationssysteme-Praktikum-1/Informationssysteme-Praktikum-1/documentation/Small-ER.png)
# Experiments
![](/Users/john/dev/Studium/Informationssysteme/Informationssysteme-Praktikum-1/Informationssysteme-Praktikum-1/documentation/Time-Results.png)
![](/Users/john/dev/Studium/Informationssysteme/Informationssysteme-Praktikum-1/Informationssysteme-Praktikum-1/documentation/Experiment-Times-Insertiontime-In-Contrast-To-Documents-In-Database.png)
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


batch 2000 with closing statements <br>
All: PT34M43.553103167S
* Reading:  PT1M25.398335878S
* Mapper:   PT2M22.937630479S
* Database: PT30M55.173235435S



## database times
* 00:00:53.745369916
* 00:01:37.116991123
* 00:02:16.236149135
* 00:02:54.039645535
* 00:03:14.60545555
* 00:03:48.635628204
* 00:03:38.577812341
* 00:04:08.544681171
* 00:04:08.167872007
* 00:04:15.503630453

// 1 = PT53.745369916S
2 = PT1M37.116991123S
3 = PT2M16.236149135S
4 = PT2M54.039645535S
5 = PT3M14.60545555S
6 = PT3M48.635628204S
7 = PT3M38.577812341S
8 = PT4M8.544681171S
9 = PT4M8.167872007S
10 = PT4M15.503630453S