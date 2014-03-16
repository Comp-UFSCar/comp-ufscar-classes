%% Changing the labels
for i=1:size(species)
    if strcmp(species{i},'setosa')
        Yline(i) = -1;
    else
        Yline(i) = 1;
    end
end

%% Using LIBSVM train and test with 80%/20%

%%Getting randomly 80% of the data for training and the rest of it for testing
joinData = [meas Yline'];
percentage = 0.4; %parameter that changes the percentage of the training data
[sample,idx] = datasample(joinData,int64(size(joinData,1)*percentage),'Replace',false);
rest = removerows(joinData,'ind',idx);
Xtesting = rest(:,1:end-1);
Ytesting = rest(:,end); 
Xtraining = sample(:,1:end-1);
Ytraining = sample(:,end);


%% Linear Kernel

%Doing the cross validation and choosing the best C on a range from 1 to
%100
maxCross = 0;
for i=1:10:100
    crossValid = svmtrain(Ytraining,Xtraining,sprintf('-t 0 -q -v 10 -c %d',i));
    % -t parameter define the kernel. "0" for linear and "2" for RBF
    if(maxCross < crossValid)
        maxCross = crossValid;
        maxC = i;
    end
end

% Picking the best C on 1 to 100 range and doing the SVM training and
% testing
model = svmtrain(Ytraining,Xtraining,sprintf('-t 0 -c %d',maxC));
[predict_label,accuracy,dec_values] = svmpredict(Ytesting,Xtesting,model);

%% RBF Kernel

%Doing the cross validation and choosing the best combination of C on a range from 1 to
%100 (increasing 10 each time) and gama on a range 0.1 to 1 (increasing 0.1 each time)
maxCross = 0;
for i=1:10:101
    for j=0.1:0.1:1
        crossValid = svmtrain(Ytraining,Xtraining,sprintf('-t 2 -q -v 10 -c %d -g %d',i,j));
        % -t parameter define the kernel. "0" for linear and "2" for RBF
        if(maxCross < crossValid)
            maxCross = crossValid;
            maxC = i;
            maxGama = j;
        end
    end
end

% Picking the best C on 1 to 100 range and doing the SVM training and
% testing
model = svmtrain(Ytraining,Xtraining,sprintf('-t 2 -c %d -g %d',maxC,maxGama));
[predict_label,accuracy,dec_values] = svmpredict(Ytesting,Xtesting,model);



