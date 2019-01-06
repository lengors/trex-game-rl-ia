function [ output_args ] = to_plot( str, var )
    fields = fieldnames(str);
    for i = 1 : length(fields)
        if strcmp(fields(i),var) == 1
            x = str.(fields{i});
            x_name = fields(i);
        end
        
    end
    y = str.scores
    
    figure
    plot(x, y);
    string = '';
    for i = 1 : length(fields)
        if strcmp(fields(i),var) == 0 && strcmp(fields(i),'scores') == 0 && i ~= length(fields)
            string = strcat(string ,{' '},  fields(i)  , ' = ' , num2str(str.(fields{i})), {' '},  ',');
        elseif strcmp(fields(i),var) == 0 && strcmp(fields(i),'scores') == 0 && i == length(fields)
            string = strcat(string ,{' '},  fields(i)  , ' = ' , num2str(str.(fields{i})), {' '});
        end
    title(string);
    xlabel(x_name); 
    ylabel('score'); 


end

% 