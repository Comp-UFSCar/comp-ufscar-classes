n<-100
wins<-rep(0,n)
for(i in 1:n)
{
  d<-sample(1:6,2,replace = T)
  if(sum(d) == 7 || sum(d) == 11) wins[i]<-1
}
f <- sum(wins)/n