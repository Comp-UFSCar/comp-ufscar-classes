x <- sample(6,1000,replace=T)
x
sum (x==3)
hist(x)

n<-c(10,100,10000)
x<-matrix(NA,6,3)
for(i in 1:6)
  for(j in 1:3)
  {
    x[i, j] <- sum(sample(1 : 6, n[j], replace=T) == i)/n[j]
  }

plot(n,x[3,])
lines(n,x[3,])