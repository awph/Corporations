drop procedure if exists fill_special_territory;

delimiter #
create procedure fill_special_territory(in input_latitude_1 double, in input_longitude_1 double, in input_latitude_2 double, in input_longitude_2 double, in quantity int unsigned)
begin
	declare quantity_added int unsigned default 0;
	set @delta = 0.01;
	start transaction;
		while quantity_added < quantity do
			set @latitude = input_latitude_1 + RAND() * (input_latitude_2 - input_latitude_1);
			set @longitude = input_longitude_1 + RAND() * (input_longitude_2 - input_longitude_1);
			while (select count(*) from territory where latitude BETWEEN @latitude - @delta AND @latitude + @delta AND longitude BETWEEN @longitude - @delta AND @longitude + @delta) > 0 do
				set @latitude = input_latitude_1 + RAND() * (input_latitude_2 - input_latitude_1);
				set @longitude = input_longitude_1 + RAND() * (input_longitude_2 - input_longitude_1);		
			end while;

			INSERT INTO location (latitude, longitude) VALUES (@latitude, @longitude);
			INSERT INTO territory (owner, location, revenue, purchasing_date, purchasing_price, sale_price) VALUES (null, LAST_INSERT_ID(), (4000 + ROUND(RAND()*6001))*10, NOW(), null, null);
			
			set quantity_added = quantity_added+1;
		end while;
	commit;
end #
delimiter ;

call fill_special_territory(40.5,5.5,50.5,8.5,500);