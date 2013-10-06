/*
 * http://www.aplus1games.com/
 * Copyright 2013, Aniruddha Loya
 * Date: 2013-10-04
 *
 * Copyright (C) 2013 by Aniruddha Loya
 *
 * Permission is hereby granted, as per the collaboration agreement for ICC competition 2013,
 * to team POWZY of this software and associated documentation files (the "Game"), to modify
 * and test it for integrating with their application. The final Game can, however, only be
 * hosted on www.aplus1games.com unless agreed upon otherwise.
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
(function(){
onload = init;

var screenW = 640,
	screenH = 808,
	scaleX, scaleY,
	xPadding = 0, yPadding = 0,
	resizeNo = 0,
	font= "TimesNewRoman",

	preLoadStart = false,
	loadComplete = false,
	ifPreLoad = true,
	loaded = 0,
	preLoadTimer = -1,

	testing = false,
	debug = true,

	images = {},
	imageData,
	stage, gameLayer, backgroundLayer, gameBg,
	mainScreen, createScreen, qrScreen, historyScreen,

	userId, gameLaunchId, userGameId,

	chkboxes = [], checkedBoxes = [0, 0, 0, 0, 0, 0, 0],
	days = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"],
	checked = 0, stake = 1, contScreen, completed = 0, existingChallenge = false, totalPacts,
	getBase = "http://powzy-service.appspot.com/rest/usergame/get/1/",
	postBase = "http://powzy-service.appspot.com/rest/usergame/submit/1/",
	QRHandler,

	author = "Aniruddha Loya", me = "aplus1games.com";

function loadGame()
{
	var info;
    if(!gameLayer)
    {
    	var newScreen,
		info = httpGet(getBase+"6?userGameId="+userGameId+"&gameLaunchId="+gameLaunchId+"&userId="+userId);
		existingChallenge = info.pactValid;
//     	console.log(userId +"\t"+ gameLaunchId +"\t"+ userGameId +"\n"+ info);

 		gameLayer = new Kinetic.Layer();

 		gameBg = new Kinetic.Image(
 		{
 			image: images.bg,
			x:0, y:0,
			width:window.innerWidth, height:window.innerHeight,
 		});
		backgroundLayer.add(gameBg);

		mainScreen = new Kinetic.Group({
			x: xPadding, y: yPadding,
			width:screenW, height:screenH,
			scale: [scaleX, scaleY]
		});
		gameLayer.add(mainScreen);
		mainScreen.welcomeScreen = true;

		var brandBar = new Kinetic.Image({
			x:0, y:48,
			image: images.brandImage,
// 			crop: getSpriteData("brand_bar.png","game1.png"),
			width: getSpriteData("brand_bar.png","game1.png")[2],
			height: getSpriteData("brand_bar.png","game1.png")[3]
		});
		mainScreen.add(brandBar);

		var headText = new Kinetic.Text({
			x:0, y: 288,
			width: screenW,
			align: "center",
			fontSize: 60,
			fill:"white",
			text: info.gameTitle,
			fontFamily: font,
		});
		mainScreen.add(headText);

		var rules = new Kinetic.Text({
			x: 0, y: 395,
			width: screenW,
			fontSize: 30,
			fill:"white",
			text:"Rules\n"+info.rules,
			fontFamily: font,
			align: 'center',
		});
		mainScreen.add(rules);

		var orangeBtnGrp = new Kinetic.Group({
			x: screenW/2, y: 603,
		});
		mainScreen.add(orangeBtnGrp);

		var wh = createImage("orange_circle_button_.png", "game1", orangeBtnGrp);

		var playText = new Kinetic.Text({
			x:0, y: wh[1]/2,
			width: wh[0],
			fontSize: 40,
			fill:"white",
			text:"Play",
			fontFamily: font,
			align: 'center',
		});
		playText.setOffsetY(playText.getHeight()/2);
		orangeBtnGrp.add(playText);

		orangeBtnGrp.setOffsetX(wh[0]/2);

		orangeBtnGrp.on("click touchend", function(){
			if(mainScreen.welcomeScreen){
				brandBar.destroy();
				mainScreen.welcomeScreen = false;
				if(existingChallenge){
					contScreen();
				}
				else
					newScreen();
			}
			else{
				mainScreen.hide();
				showHistory();
			}
			gameLayer.draw();
		});

		var createGrp = new Kinetic.Group({
			x: screenW/2, y: 469,
		});
		createGrp.hide();

		createGrp.on("click touchend", function(){
			if(this.contScreen)
			{
				mainScreen.hide();
				showQRScreen();
			}
			else {
				mainScreen.hide();
				showCreateScreen();
			}
		});

		wh = createImage("progress_base.png","game1", createGrp);

		var createBtn = new Kinetic.Image({
			x: wh[0]/2, y : wh[1]/2,
			image: images.game1,
			crop: getSpriteData("progress_button_.png","game1.png"),
			width: getSpriteData("progress_button_.png","game1.png")[2],
			height: getSpriteData("progress_button_.png","game1.png")[3],
		});
		createBtn.setOffset(createBtn.getWidth()/2, createBtn.getHeight()/2);
		createGrp.add(createBtn);

		var createText = new Kinetic.Text({
			x:0, y: wh[1]/2,
			width: wh[0],
			fontSize: 40,
			fill:"white",
			text:"Create\nChallenge",
			fontFamily: font,
			align: 'center'
		});
		createGrp.add(createText);
		createText.setOffsetY(createText.getHeight()/2);

		createGrp.setOffsetX(wh[0]/2);

		var redBand = new Kinetic.Image({
			x: wh[0]/2, y : 8,
			image: images.game1,
			crop: getSpriteData("progress_redbar.png","game1.png"),
			width: getSpriteData("progress_redbar.png","game1.png")[2],
			height: getSpriteData("progress_redbar.png","game1.png")[3]
		});
		redBand.hide();
		redBand.setOffsetX(redBand.getWidth()/2);
		createGrp.add(redBand);

		var greenBand = new Kinetic.Image({
			x: 10, y : 9,
			image: images.game1,
			crop: getSpriteData("progress_greenbar.png","game1.png"),
			width: getSpriteData("progress_greenbar.png","game1.png")[2],
			height: getSpriteData("progress_greenbar.png","game1.png")[3]
		});
		greenBand.hide();
		createGrp.add(greenBand);

		var yellowBand = new Kinetic.Image({
			x: wh[0]/2, y : 8,
			image: images.game1,
			crop: getSpriteData("progress_yellowbar.png","game1.png"),
			width: getSpriteData("progress_yellowbar.png","game1.png")[2],
			height: getSpriteData("progress_yellowbar.png","game1.png")[3]
		});
		yellowBand.hide();
		createGrp.add(yellowBand);

		mainScreen.add(createGrp);

		var challengeInfoGroup = new Kinetic.Group({
			x: 0, y: 286,
		});
		challengeInfoGroup.hide();
		mainScreen.add(challengeInfoGroup);

		var leftBtn = new Kinetic.Image({
			x: 65, y: 0,
			image: images.game1,
			crop: getSpriteData("orange_button.png","game1.png"),
			width: getSpriteData("orange_button.png","game1.png")[2],
			height: getSpriteData("orange_button.png","game1.png")[3]
		});
		challengeInfoGroup.add(leftBtn);

		var leftText = new Kinetic.Text({
			x: leftBtn.getX(), y: leftBtn.getHeight()/2,
			width: leftBtn.getWidth(),
			fontSize: 30,
			fill:"white",
			text:"",
			fontFamily: font,
			align: 'center'
		});
		leftText.setOffsetY(leftText.getHeight()/2);
		challengeInfoGroup.add(leftText);

		var rightBtn = new Kinetic.Image({
			x: 342, y: 0,
			image: images.game1,
			crop: getSpriteData("orange_button.png","game1.png"),
			width: getSpriteData("orange_button.png","game1.png")[2],
			height: getSpriteData("orange_button.png","game1.png")[3]
		});
		challengeInfoGroup.add(rightBtn);

		var rightText = new Kinetic.Text({
			x: rightBtn.getX(), y: rightBtn.getHeight()/2,
			width: rightBtn.getWidth(),
			fontSize: 30,
			fill:"white",
			text:"",
			fontFamily: font,
			align: 'center'
		});
		rightText.setOffsetY(rightText.getHeight()/2);
		challengeInfoGroup.add(rightText);

		stage.add(gameLayer);

		newScreen = function()
		{
			createGrp.contScreen = false;
			headText.hide();

			rules.setText("Welcome!\nAre you ready for the next challenge?");
			rules.setPosition({y: 291});

			orangeBtnGrp.setPosition({y:40});
			playText.setText("My\nWorkouts");
			playText.setFontSize(30);
			playText.setOffsetY(playText.getHeight()/2);

			createGrp.show();
		}

		contScreen = function()
		{
			var wh, finished = Math.min(100,Math.floor(completed / checked * 100));
			if(finished > 0)
			{
				yellowBand.show();
				wh = getSpriteData("progress_yellowbar.png","game1.png");
				if(finished < 40)
				{
					yellowBand.setHeight(wh[3] * finished / 40);
					yellowBand.setCropHeight(wh[3] * finished / 40);
				}
				else
				{
					yellowBand.setHeight(wh[3]);
					yellowBand.setCropHeight(wh[3]);
				}
			}
			if(finished > 40)
			{
				redBand.show();
				wh = getSpriteData("progress_redbar.png","game1.png");
				if(finished <= 50)
				{
 					redBand.setWidth(wh[2] * finished / 100);
					redBand.setCropWidth(wh[2] * finished / 100);
					redBand.setCropX(wh[0] + wh[2] * (1 - finished / 100));
					redBand.setOffsetX(wh[2] * ( -1 / 2 + finished / 100));
				}
				else if(finished <= 70)
				{
 					redBand.setWidth(wh[2] * (finished+10) / 100);
					redBand.setCropWidth(wh[2] * (finished+10) / 100);
					redBand.setCropX(wh[0] + wh[2] * (1 - (finished+10) / 100));
					redBand.setOffsetX(wh[2] * ( -1 / 2 + (finished+10) / 100));
				}
				else
				{
 					redBand.setWidth(wh[2]);
					redBand.setCropWidth(wh[2]);
					redBand.setCropX(wh[0]);
					redBand.setOffsetX(wh[2] /2);
				}
			}
			if(finished > 75)
			{
				greenBand.show();
				wh = getSpriteData("progress_greenbar.png","game1.png");
				if(finished < 95)
				{
					greenBand.setHeight(wh[3] * (finished - 75) / 20);
					greenBand.setCropHeight(wh[3] * (finished - 75) / 20);
					greenBand.setY(9 + wh[3] * (1 - (finished - 75) / 20));
					greenBand.setCropY(wh[1] + wh[3] * (1 - (finished - 75) / 20));
				}
				else
				{
					greenBand.setHeight(wh[3]);
					greenBand.setCropHeight(wh[3]);
				}
			}

			createGrp.contScreen = true;
			headText.setText("Current Weekly Challenge");
			headText.setFontSize(30);
			headText.setPosition({y: 250});
			headText.show();

			if(finished < 100)
				rules.setText("You can do it!");
			else
				rules.setText("You finished the challenge\nCongratulations!");
			rules.setPosition({y: 360});

			leftText.setText(completed+"/"+checked+" workouts");

			rightText.setText("CHF "+stake);

			orangeBtnGrp.setPosition({y:40});
			playText.setText("My\nWorkouts");
			playText.setFontSize(30);
			playText.setOffsetY(playText.getHeight()/2);

			createGrp.show();

			if(finished < 100)
				createText.setText(finished+"%\nContinue the\nchallenge");
			else
				createText.setText(finished+"%\nChallenge \nComplete!");
			createText.setOffsetY(createText.getHeight()/2);

			challengeInfoGroup.show();

			gameLayer.draw();
		}
	}
	else
	{
		gameLayer.show();
		mainScreen.show();
	}

	if(userGameId != -1)
	{
		info = httpGet(getBase+"5?userGameId="+userGameId+"&gameLaunchId="+gameLaunchId+"&userId="+userId+ "&level=-1");
		checkedBoxes = info.level.committedWorkDays;
		stake = info.level.wager;
		totalPacts = info.totalPact;
		for(var i = 0; i < info.level.workOutDays.length; i++)
		{
			if(info.level.workOutDays[i])
				completed++;
			if(checkedBoxes[i] === 1)
				checked++;
		}
	}
	if(testing)
	{
		existingChallenge = true;
		contScreen();
	}

	stage.draw();
}

function showQRScreen()
{
	if(!qrScreen)
	{
		qrScreen = new Kinetic.Group({
			x: xPadding, y: yPadding,
			width:screenW, height:screenH,
			scale: [scaleX, scaleY]
		});
		gameLayer.add(qrScreen);

		var qrbracket = new Kinetic.Image({
			x:121, y:236,
			image: images.game1,
			crop: getSpriteData("qrscanner.png","game1.png"),
			width: getSpriteData("qrscanner.png","game1.png")[2],
			height: getSpriteData("qrscanner.png","game1.png")[3]
		});
		qrScreen.add(qrbracket);

		var btnGrp = new Kinetic.Group({
			x: screenW/2, y: 28,
		});
		qrScreen.add(btnGrp);

		wh = createImage("orange_button.png","game1", btnGrp);
		btnGrp.setOffsetX(wh[0]/2);

		var text = new Kinetic.Text({
			x:0, y:wh[1]/2,
			width: wh[0],
			fontSize: 25,
			fill:"black",
			text: "Scan",
			fontFamily: font,
			align: "center"
		});
		text.setOffsetY(text.getHeight()/2);
		btnGrp.add(text);

		btnGrp.on("click touchend", function(){

			if(text.getText() === "Checked In")
				return;

			try{
				var xmlHttp = new XMLHttpRequest();

				xmlHttp.onreadystatechange = function(){
					if(xmlHttp.readyState === 4)
					{
					}
				};

				xmlHttp.open( "GET", "http://myApp.example.org/qrreader", true );
				xmlHttp.send( null );
					var params = {
				  "userGameId": userGameId,
				  "gameLaunchId": gameLaunchId,
				  "userId": userId,
				  "currentDate": (new Date()).getTime(),
				}
				text.setText("Checked In");
				completed++;
				gameLayer.draw();

			}
			catch(e){
				console.log("Catched a thrown exception"+e);
				QRHandler("err")//JSON.parse('{"gameLaunchId":"5714368087982080"}'));
			}
		});
		var backBtnGrp = new Kinetic.Group({
			x: 270, y: 236 + qrbracket.getWidth() + 28,
		});
		qrScreen.add(backBtnGrp);

		var wh = createImage("orange_small_button.png","game1", backBtnGrp);

		var backText = new Kinetic.Text({
			x:0, y:wh[1]/2,
			width: wh[0],
			fontSize: 25,
			fill:"black",
			text: "Back",
			fontFamily: font,
			align: "center"
		});
		backText.setOffsetY(backText.getHeight()/2);
		backBtnGrp.add(backText);

		backBtnGrp.on("click touchend", function(){
			text.setText("Scan");
			qrScreen.hide();
			mainScreen.show();
			contScreen();
			gameLayer.draw();
		});

	}
	else
		qrScreen.show();
	gameLayer.draw();
}

function createImage(imageNm, spriteNm, grp)
{
	var image = new Kinetic.Image({
		x:0, y:0,
		image: images[spriteNm],
		crop: getSpriteData(imageNm, spriteNm+".png"),
		width: getSpriteData(imageNm, spriteNm+".png")[2],
		height: getSpriteData(imageNm, spriteNm+".png")[3]
	});
	grp.add(image);
	return [image.getWidth(), image.getHeight()];
}

function showHistory()
{
	var info =
	httpGet(getBase+"5?userGameId="+userGameId+"&gameLaunchId="+gameLaunchId+"&userId="+userId+"&level="+(totalPacts-2));

	gameBg.setImage(images.status);
	if(!historyScreen)
	{
		historyScreen = new Kinetic.Group({
			x: xPadding, y: yPadding,
			width:screenW, height:screenH,
			scale: [scaleX, scaleY]
		});
		gameLayer.add(historyScreen);

		var summaryGrpRed = new Kinetic.Group({
			x: 29, y: 51,
		});
		historyScreen.add(summaryGrpRed);

		var wh = createImage("red_circle_.png","game1", summaryGrpRed);

		var workSummary = new Kinetic.Text({
			x:0, y:wh[1]/2,
			width: wh[0],
			align: "center",
			fontSize: 40,
			fill:"white",
			text: "",
			fontFamily: font
		});
		workSummary.setOffsetY(workSummary.getHeight()/2);
		summaryGrpRed.add(workSummary);

		var summaryGrpY = new Kinetic.Group({
			x: 445, y: 51,
		});
		historyScreen.add(summaryGrpY);

		wh = createImage("yellow_circle_.png", "game1", summaryGrpY);

		var earningsSummary = new Kinetic.Text({
			x:0, y:wh[1]/2,
			width: wh[0],
			align: "center",
			fontSize: 40,
			fill:"white",
			text: "",
			fontFamily: font
		});
		earningsSummary.setOffsetY(earningsSummary.getHeight()/2);
		summaryGrpY.add(earningsSummary);

		var summaryText = new Kinetic.Text({
			x:screenW/2, y: 51+wh[1]/2,
			fontSize: 40,
			fill:"white",
			text: "Summary",
			fontFamily: font
		});
		summaryText.setOffset(summaryText.getWidth()/2, summaryText.getHeight()/2);
		historyScreen.add(summaryText);

		var ty = 145 + (51+wh[1])/2; // y = 51+wh[1] + (290 - (51+wh[1]) )/2
		var stakeText = new Kinetic.Text({
			x:screenW/2, y: ty,
			fontSize: 30,
			fill:"white",
			text: "Stake",
			fontFamily: font
		});
		stakeText.setOffset(stakeText.getWidth()/2, stakeText.getHeight()/2);
		historyScreen.add(stakeText);

		var workText = new Kinetic.Text({
			x: summaryGrpRed.getX() + wh[0]/2, y: ty,
			fontSize: 30,
			fill:"white",
			text: "Workouts",
			fontFamily: font
		});
		workText.setOffset(workText.getWidth()/2, workText.getHeight()/2);
		historyScreen.add(workText);

		var earnText = new Kinetic.Text({
			x: summaryGrpY.getX() + wh[0]/2, y: ty,
			fontSize: 30,
			fill:"white",
			text: "Earnings (CHF)",
			fontFamily: font
		});
		earnText.setOffset(earnText.getWidth()/2, earnText.getHeight()/2);
		historyScreen.add(earnText);

		var weekGrpRed = new Kinetic.Group({
			x: 29, y: 290,
		});
		historyScreen.add(weekGrpRed);

		wh = createImage("red_circle_.png","game1", weekGrpRed);

		var workWeek = new Kinetic.Text({
			x:0, y:wh[1]/2,
			width: wh[0],
			align: "center",
			fontSize: 40,
			fill:"white",
			text: "-",
			fontFamily: font
		});
		workWeek.setOffsetY(workWeek.getHeight()/2);
		weekGrpRed.add(workWeek);

		var weekGrpY = new Kinetic.Group({
			x: 445, y: 290,
		});
		historyScreen.add(weekGrpY);

		wh = createImage("yellow_circle_.png","game1", weekGrpY);

		var weekEarnings = new Kinetic.Text({
			x:0, y:wh[1]/2,
			width: wh[0],
			align: "center",
			fontSize: 40,
			fill:"white",
			text: "-",
			fontFamily: font
		});
		weekEarnings.setOffsetY(weekEarnings.getHeight()/2);
		weekGrpY.add(weekEarnings);

		var weekGrpGreen = new Kinetic.Group({
			x: 236, y: 290,
		});
		historyScreen.add(weekGrpGreen);

		wh = createImage("green_circle_.png","game1", weekGrpGreen);

		var weekStake = new Kinetic.Text({
			x:0, y:wh[1]/2,
			width: wh[0],
			fontSize: 40,
			fill:"white",
			text: "-",
			fontFamily: font,
			align: "center"
		});
		weekStake.setOffsetY(weekStake.getHeight()/2);
		weekGrpGreen.add(weekStake);

		var lastWeekText = new Kinetic.Text({
			x:screenW/2, y: wh[1]+290+50,
			fontSize: 40,
			fill:"white",
			text: "Previous Pact",
			fontFamily: font
		});
		lastWeekText.setOffset(lastWeekText.getWidth()/2, lastWeekText.getHeight()/2);
		historyScreen.add(lastWeekText);

		var orangeBtn = new Kinetic.Group({
			x: 236, y: 601,
		});
		historyScreen.add(orangeBtn);

		wh = createImage("orange_circle_button_.png","game1", orangeBtn);

		var btnText = new Kinetic.Text({
			x:wh[0]/2, y:wh[1]/2,
			fontSize: 40,
			fill:"white",
			text: "Back",
			fontFamily: font
		});
		btnText.setOffset(btnText.getWidth()/2, btnText.getHeight()/2);
		orangeBtn.add(btnText);

		orangeBtn.on("click touchend", function(){
			gameBg.setImage(images.bg);
			historyScreen.hide();
			mainScreen.show();
			stage.draw();
		});

		historyScreen.update = function()
		{
			if(totalPacts > 1)
			{
				weekStake.setText(info.level.wager);
				weekEarnings.setText(info.level.winningPrize);
				var cnt = 0;
				for(var i = 0; i < info.level.workOutDays.length; i++)
					if(info.level.workOutDays[i])
						cnt++;
				workWeek.setText(cnt);
			}
			workSummary.setText(info.totalWorkout);
			earningsSummary.setText(info.totalEarning);
		}
	}
	else
		historyScreen.show();

 	historyScreen.update();
	stage.draw();
}

function showCreateScreen()
{
	if(!createScreen)
	{
		createScreen = new Kinetic.Group({
			x: xPadding, y: yPadding,
			width:screenW, height:screenH,
			scale: [scaleX, scaleY]
		});
		gameLayer.add(createScreen);

		var bg = new Kinetic.Image({
			x:102, y:20,
			image: images.game1,
			crop: getSpriteData("notebook.png","game1.png"),
			width: getSpriteData("notebook.png","game1.png")[2],
			height: getSpriteData("notebook.png","game1.png")[3]
		});
		createScreen.add(bg);

		var newChkBox = function(n){
			var box = new Kinetic.Image({
				image: images.game1,
				x: 144, y: (156+n*58),
				crop: getSpriteData("checkbox.png","game1.png"),
				width: getSpriteData("checkbox.png","game1.png")[2],
				height: getSpriteData("checkbox.png","game1.png")[3]
			});
			box.bno = n;
			box.setAttr('checked', false);
			box.on("click touchend", function(){
				if(box.getAttr('checked'))
				{
					checkedBoxes[box.bno] = 0;
					box.setCrop(getSpriteData("checkbox.png","game1.png"));
					box.setAttr('checked', false);
					checked--;
				}
				else
				{
					checkedBoxes[box.bno] = 1;
					box.setCrop(getSpriteData("checkbox_on.png","game1.png"));
					box.setAttr('checked', true);
					checked++;
				}
				box.setWidth(box.getCropWidth());
				box.setHeight(box.getCropHeight());
				createScreen.draw();
			});
			createScreen.add(box);

			return box;
		};

		var dayText = function(n){
			var text = new Kinetic.Text({
				x:211, y:(164+n*58),
				fontSize: 25,
				fill:"black",
				text: days[n],
				fontFamily: font
			});
			createScreen.add(text);
		};

		for (var i=0; i < 7; i++)
		{
			chkboxes[i] = newChkBox(i);
			dayText(i);
		}

		var confirmBtn = new Kinetic.Group({
			x: screenW/2, y: 710,
		});
		createScreen.add(confirmBtn);

		var wh = createImage("orange_button.png","game1", confirmBtn);

		var confirmText = new Kinetic.Text({
			x:0, y:wh[1]/2,
			width: wh[0],
			fontSize: 25,
			fill:"white",
			text: "Confirm Stake",
			fontFamily: font,
			align: "center"
		});
		confirmText.setOffsetY(confirmText.getHeight()/2);
		confirmBtn.add(confirmText);
		confirmBtn.setOffsetX(wh[0]/2);

		confirmBtn.on("click touchend", function(){
			if(checked > 0)
			{
				var params = {
				  "userGameId": userGameId,
				  "gameLaunchId": gameLaunchId,
				  "userId": userId,
				  "commitedWorkOutdays": checkedBoxes,
				  "startDate": (new Date()).getTime(),
				  "wager": stake
				}
				var info = httpPost(postBase+"3", params);
				if(!info)
				{
					confirmText.setText("Retry");
					gameLayer.draw();
					return;
				}
				confirmText.setText("Confirm Stake");
				userGameId = info.userGameId;
				totalPacts = info.totalLevel;
				completed = 0;
				createScreen.hide();
				existingChallenge = true;
				mainScreen.show();
				contScreen();
			}
		});

		var amtText = new Kinetic.Text({
			x: screenW/2, y: 636,
			fontSize: 30,
			fill:"white",
			text: "CHF 1",
			fontFamily: font
		});
		createScreen.add(amtText);
		amtText.setOffsetX(amtText.getWidth()/2);

		var updateStake = function()
		{
			stake += this.increment;
			if(stake < 1)
				stake = 1;
			amtText.setText("CHF "+stake);
			amtText.setOffsetX(amtText.getWidth()/2);
			gameLayer.draw();
		};

		var minus = new Kinetic.Image({
			x: screenW/2 - wh[0]/2 - 20, y: 628,
// 			scale: [scaleX, scaleY],
			image: images.game1,
			crop: getSpriteData("stake_minus.png","game1.png"),
			width: getSpriteData("stake_minus.png","game1.png")[2],
			height: getSpriteData("stake_minus.png","game1.png")[3]
		});
		createScreen.add(minus);
		minus.increment = -1;
		minus.on("click touchend", updateStake);

		var plus = new Kinetic.Image({
			x: screenW/2 + wh[0]/2 + 20, y: 628,
// 			scale: [scaleX, scaleY],
			image: images.game1,
			crop: getSpriteData("stake_plus.png","game1.png"),
			width: getSpriteData("stake_plus.png","game1.png")[2],
			height: getSpriteData("stake_plus.png","game1.png")[3]
		});
		createScreen.add(plus);
		plus.setOffsetX(plus.getWidth());
		plus.increment = 1;
		plus.on("click touchend", updateStake);
	}
	else{
	}
	gameLayer.draw();
}

function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
}

function httpGet(url)
{
	try{
		var xmlHttp = new XMLHttpRequest();
		xmlHttp.open( "GET", url, false );
		xmlHttp.send( null );
		return JSON.parse(xmlHttp.responseText);
    }
    catch(e){
    	console.log(e);
    	return null;
	}
}

function httpPost(url, params)
{
    try{
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "POST", url, false );
    xmlHttp.setRequestHeader('Content-type', 'application/json');
    xmlHttp.send( JSON.stringify(params) );
    return JSON.parse(xmlHttp.responseText);
    }
    catch(e){
    	return null;
    }
}

function preLoader()
{
    if(!preLoadStart)
    {
        preLoadStart = true;
        var imgToLoad = 2, imgLoaded = 0;
		stage = new Kinetic.Stage({container:"game", width:window.innerWidth, height:window.innerHeight});

        backgroundLayer = new Kinetic.Layer();

        var onImgLoad = function()
        {
            imgLoaded++;
            if(imgLoaded == imgToLoad)
            {
            	var bgCol = new Kinetic.Rect(
            	{
            		x:0, y:0, width:window.innerWidth, height:window.innerHeight,
            		fill:"black"
            	});
            	backgroundLayer.add(bgCol);

                var preLoadBg = new Kinetic.Image(
                {
                	image: bg,
                	x: window.innerWidth/2-128, y:window.innerHeight/2-128,
                });
                backgroundLayer.add(preLoadBg);

                var brandText = new Kinetic.Text(
                {
                	fontFamily: "BookmanOldStyle",
                	text: 'www.aplus1games.com',
                	x: window.innerWidth/2, y:window.innerHeight/2+150*scaleY,
                	stroke: "white",
                	fontSize: 40,
                });
                brandText.setOffsetX(brandText.getWidth()/2);
                brandText.on("click touchend", function(){
                	window.open("http://www.aplus1games.com");
                });
                backgroundLayer.add(brandText);

				loaded = (debug? 0.95: 0);

                var loadingText = new Kinetic.Text(
                {
                	fontFamily: "BookmanOldStyle",
                	text: 'Loading: '+Math.round(loaded*100)+"%",
                	x: window.innerWidth/2, y:window.innerHeight/2+250*scaleY,
                	stroke: "white",
                	fontSize: 25,
                });
                loadingText.setOffsetX(loadingText.getWidth()/2);
                backgroundLayer.add(loadingText);

                var loadProgressBar = new Kinetic.Rect(
                {
                	x: window.innerWidth/2-200*scaleX, y:window.innerHeight/2+300*scaleY,
                	width: 1,
                	height: 15*scaleY,
                	fill: "white"
                });
                backgroundLayer.add(loadProgressBar);
                stage.add(backgroundLayer);
                stage.draw();

                preLoadTimer = setInterval(function()
                {
                	loadingText.setText("Loading: "+Math.round(loaded*100)+"%");
					loadProgressBar.setWidth(400*scaleX*loaded);
					backgroundLayer.draw();
					if (loadComplete)
					{
						clearInterval(preLoadTimer);
						preLoadTimer = -10;
						loadComplete = false;
						backgroundLayer.destroyChildren();
						loadGame();
						/* Aniruddha Loya*/
					}
                },10);

            }
        };

        var bg = new Image();
        bg.onload = onImgLoad;
        bg.src = 'images/aplus1.png';

		imageData = "js/atlas.js";

		var node = document.createElement('script');
		node.src = imageData;
		document.body.appendChild(node);
		var onld = function()
		{
			imageData = sprites;
			onImgLoad();
		}
		node.onload = onld;
    }
}

function getSpriteData(imageName, spritesheetName){
	var data = imageData[spritesheetName].frames[imageName].frame;
	return [data.x,data.y,data.w,data.h];
};

function loadImages(sources, callback)
{
    var loadedImages = 0;
    var numImages = 0;
    for (var src in sources) {
        numImages++;
    }
    for (var src in sources) {
        images[src] = new Image();
        images[src].onload = function(){
            if (++loadedImages >= numImages) {
                if(ifPreLoad)
                    loadComplete=true;
                else
                    callback();//(images);
            }
            loaded = loadedImages/numImages;
        };
        images[src].src = sources[src];
    }
}

function resizeGame()
{
    var screenH1 = window.innerHeight,
    	screenW1 = window.innerWidth;

    scaleX = screenW1 / screenW;
    scaleY = screenH1 / screenH;

    if (scaleX == scaleY)
    {
    	xPadding = (screenW1 - screenW * scaleX) / 2;
    	yPadding = (screenH1 - screenH * scaleY) / 2;
	}
    else if (scaleX > scaleY)
    {
    	// means W > H ==> x-padding
    	scaleX = scaleY;
    	xPadding = (screenW1 - screenW * scaleX) / 2;
    	yPadding = (screenH1 - screenH * scaleY) / 2;
	}
    else
    {
    	// means W < H ==> y-padding
    	scaleY = scaleX;
    	xPadding = (screenW1 - screenW * scaleX) / 2;
    	yPadding = (screenH1 - screenH * scaleY) / 2;
    }

	if(stage)
	{
		stage.setSize(screenW1, screenH1);

		if(gameBg){
			gameBg.setWidth(screenW1);
			gameBg.setHeight(screenH1);
		}

		if(mainScreen){
			mainScreen.setPosition(xPadding, yPadding);
			mainScreen.setScale(scaleX);

			if(createScreen){
				createScreen.setPosition(xPadding, yPadding);
				createScreen.setScale(scaleX);
			}

			if(qrScreen){
				qrScreen.setPosition(xPadding, yPadding);
				qrScreen.setScale(scaleX);
			}

			if(historyScreen){
				historyScreen.setPosition(xPadding, yPadding);
				historyScreen.setScale(scaleX);
			}
		}

		stage.draw();
	}

	resizeNo = 2;

}

window.onresize = function(){resizeGame();}
window.onorientationchange = function(){resizeGame();}

function init()
{
	resizeGame();
    if(resizeNo > 1)
    {
		preLoader();
		var ulrVars = getUrlVars();
    	userId = ulrVars["userId"];
    	gameLaunchId = ulrVars["gameLaunchId"];
    	userGameId = ulrVars["userGameId"];

		var info = httpGet(getBase+"6?userGameId="+userGameId+"&gameLaunchId="+gameLaunchId+"&userId="+userId);

		var sources = {

			game1: "images/game1.png",
			status: "images/background_status.png",
			bg: "images/background.png",
			brandImage: info.brandUrl
		};
		loadImages(sources,loadGame);
	}
}
})();
