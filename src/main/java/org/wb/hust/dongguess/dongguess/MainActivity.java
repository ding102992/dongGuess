package org.wb.hust.dongguess.dongguess;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.wb.hust.dongguess.customui.CustomGridView;
import org.wb.hust.dongguess.data.Const;
import org.wb.hust.dongguess.modal.IAlertDialogButtonListener;
import org.wb.hust.dongguess.modal.IWordButtonClickListener;
import org.wb.hust.dongguess.modal.Song;
import org.wb.hust.dongguess.modal.WordButton;
import org.wb.hust.dongguess.util.MusicPlayer;
import org.wb.hust.dongguess.util.Util;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;

public class MainActivity extends BaseActivity implements IWordButtonClickListener{
    private static final String TAG = "dongdong";

    /**
     * 答案状态 ：正确
     */
    private static final int ANS_RIGHT = 1;

    /**
     * 答案状态 ：错误
     */
    private static final int ANS_WRONG = -1;

    /**
     * 答案状态 ：不完整
     */
    private static final int ANS_LACK = 0;

    /**
     * 答案错误闪烁次数
     */
    private static final int SPARK_TIME = 6;

    /**
     * 删除错误答案弹出框id(自定义)
     */
    private static final int ID_DIALOG_DELETE_WORD = 0;

    /**
     * 提示答案弹出框id(自定义)
     */
    private static final int ID_DIALOG_TIP_WORD = 1;

    /**
     * 金币不足弹出框提示(自定义)
     */
    private static final int ID_DIALOG_LACK_COIN = 2;



    //唱片相关动画
    private Animation mPanAnim;
    private LinearInterpolator mPanLin;
    private Animation mBarInAnim;
    private LinearInterpolator mBarInLin;
    private Animation mBarOutAnim;
    private LinearInterpolator mBarOutLin;

    private ImageView mViewPan;

    private ImageView mViewPanBar;
    // play 按键事件
    private ImageButton mBtnPlayStart;
    private boolean mIsRunning;

    //待选框内所有的字
    private ArrayList<WordButton> mAllWords;
    //待选框内所有的字 - 容器
    private CustomGridView mCustomGridView;

    //已选择的字
    private ArrayList<WordButton> mSelectWords;
    //已选择的字 - 容器
    private LinearLayout mViewSelectedWords;

    //当前的歌曲
    private Song mCurrentSong;
    //当前的关数
    private int mCurrentStageIndex = -1;
    //当前关数 - 容器
    private TextView mTxtCurrentStageIndex;

    //当前金币数
    private int mCurrentCoins = Const.TOTAL_COINS;
    //当前金币数 - 容器
    private TextView mViewCurrentCoins;

    //提示按钮
    private ImageButton mBtnTip;
    //删除错误答案按钮
    private ImageButton mBtnDel;

    //过关界面
    private LinearLayout mViewPassStage;
    //过关界面显示效果
    private Animation mScaleAnim;
    private boolean mIsPassStageShown = false;  //过关界面显示标志位
    private TextView mViewPassStageIndex ;
    private TextView mViewPassStageSongName;
    private ImageButton mBtnNextStage;
    private ImageButton mBtnShareWeChat;
    private ImageButton mBtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //初始化Bmob
        Bmob.initialize(this,Const.BMOB_APPLICATION_KEY);
        // 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation(this).save();
        // 启动推送服务
        BmobPush.startWork(this, Const.BMOB_APPLICATION_KEY);

        //读取游戏数据
        int[] gameData = Util.readData(this);
        mCurrentStageIndex = gameData[Const.INDEX_DATA_CURRENT_STAGE];
        mCurrentCoins = gameData[Const.INDEX_DATA_CURRENT_CONIS];

        mViewPan = (ImageView) findViewById(R.id.pan_view);
        mViewPanBar = (ImageView) findViewById(R.id.bar_view);
        mCustomGridView = (CustomGridView) findViewById(R.id.word_selector);
        mCustomGridView.registerWordButtonClickEvents(this);
        mViewSelectedWords = (LinearLayout) findViewById(R.id.selected_word_container);
        mViewCurrentCoins = (TextView) findViewById(R.id.txt_current_coin);
        mViewCurrentCoins.setText(mCurrentCoins +"");

        //初始化动画
        mPanAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        mPanLin = new LinearInterpolator();
        mPanAnim.setInterpolator(mPanLin);
        mPanAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mViewPanBar.startAnimation(mBarOutAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mBarInAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_45);
        mBarInAnim.setFillAfter(true);
        mBarInLin = new LinearInterpolator();
        mBarInAnim.setInterpolator(mBarInLin);
        mBarInAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mViewPan.startAnimation(mPanAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mBarOutAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_d_45);
        mBarOutAnim.setFillAfter(true);
        mBarOutLin = new LinearInterpolator();
        mBarOutAnim.setInterpolator(mBarOutLin);
        mBarOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mIsRunning = false;
                mBtnPlayStart.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mBtnPlayStart = (ImageButton) findViewById(R.id.play_start_btn);
        mBtnPlayStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePlayButton();
            }
        });

        mBtnTip = (ImageButton) findViewById(R.id.btn_tip);
        mBtnTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog(ID_DIALOG_TIP_WORD);
            }
        });

        mBtnDel = (ImageButton) findViewById(R.id.btn_del);
        mBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog(ID_DIALOG_DELETE_WORD);
            }
        });
        initCurrentStageData();
        mBtnBack = (ImageButton)findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });
    }

    /**
     * 点击播放按钮事件
     */
    private void handlePlayButton() {
        if (mViewPanBar != null && !mIsRunning) {
            mIsRunning = true;
            mBtnPlayStart.setVisibility(View.INVISIBLE);
            mViewPanBar.startAnimation(mBarInAnim);
            MusicPlayer.playMusic(MainActivity.this,mCurrentSong.getFileName());
        }
    }

    /**
     * 点提示按钮事件
     */
    private void handleTipButton(){
        //如果金币数不足，则不执行
        int tipCost = getTipCost();
        if(!handleCurrentCoinCount(-tipCost)){
            showConfirmDialog(ID_DIALOG_LACK_COIN);
            return;
        }
        boolean hasFound = false;
        //如果有空的填充位置，则执行
        for(int i = 0; i < mSelectWords.size(); i++){
            if("".equals(mSelectWords.get(i).mText)){
                //找到当前索引处的正确答案按钮,并调用点击事件
                onWordButtonClick(getAnswerWordButton(i));
                hasFound = true;
                break;
            }
        }
        if(!hasFound){
            handleCurrentCoinCount(tipCost);
        }
    }

    /**
     * 获取提示答案功能花费金币
     * @return
     */
    private int getTipCost(){
        return getResources().getInteger(R.integer.tip_cost);
    }

    private WordButton getAnswerWordButton(int index){
        for(int i=0; i<mAllWords.size(); i++){
            if(mAllWords.get(i).mText.equals(mCurrentSong.getNameCharacters()[index]+"")){
                return mAllWords.get(i);
            }
        }
        return null;
    }

    private WordButton getNotAnswerWordButton(){
        Random random = new Random();
        int i = 0 ; //保险起见，循环一千次最多
        while(i++ < 1000){
            int index = random.nextInt(CustomGridView.COUNTS_WORDS);
            if(!mCurrentSong.getSongName().contains(mAllWords.get(index).mText) && mAllWords.get(index).mIsVisiable){
//            MyLog.i(TAG,"index="+index);
                return mAllWords.get(index);
            }
        }
        return null;
    }

    /**
     * 处理删除一个错误事件
     */
    private void handleDelButton(){
        int delCost = getDelCost();
        if(!handleCurrentCoinCount(-delCost)){
            showConfirmDialog(ID_DIALOG_LACK_COIN);
            return;
        }
        //找到一个非正确答案的按钮
        WordButton wb = getNotAnswerWordButton();
        if(wb == null){
            handleCurrentCoinCount(delCost);
            return ;
        }
        //设置隐藏
        setWordButtonVisibility(wb,View.INVISIBLE);
    }

    /**
     * 获取删除错误答案功能的花费金币数
     * @return
     */
    private int getDelCost(){
        return getResources().getInteger(R.integer.del_cost);
    }


    /**
     * 处理当前金币数，增加或减少
     * @param count 可以为正数，负数
     * @return 成功true,失败false
     */
    private boolean handleCurrentCoinCount(int count){
        if((mCurrentCoins + count) < 0){
            return false;
        }
        mCurrentCoins += count;
        mViewCurrentCoins.setText(mCurrentCoins +"");
        return true;
    }

    @Override
    protected void onPause() {
        mViewPan.clearAnimation();
        MusicPlayer.stopMusic();
        // 保存游戏的数据
        Util.writeData(this, mCurrentStageIndex - 1, mCurrentCoins);
        super.onPause();
    }

    /**
     * 生成所有的待选文字
     * @return
     */
    private String[] generateWords(){
        Random random = new Random();
        String[] words = new String[CustomGridView.COUNTS_WORDS];
        //存入歌名
        for(int i = 0 ; i < mCurrentSong.getNameLength(); i++){
            words[i] = mCurrentSong.getNameCharacters()[i] + "";
        }
        //填充随机文字
        for(int i = mCurrentSong.getNameLength(); i < CustomGridView.COUNTS_WORDS; i++){
            words[i] = Util.getRandomChinese()+"";
        }

        //打乱文字顺序
        for (int i = CustomGridView.COUNTS_WORDS -1; i >= 0; i--){
            int index = random.nextInt(i + 1);
            String buf = words[index];
            words[index] = words[i];
            words[i] = buf;
        }

        return words;
    }

    /**
     * 初始化当前关数的数据
     */
    private void initCurrentStageData() {
        // 获得数据
        mCurrentSong = loadStageSongInfo(++mCurrentStageIndex);
        mTxtCurrentStageIndex = (TextView) findViewById(R.id.txt_current_stage);
        mTxtCurrentStageIndex.setText(mCurrentStageIndex + 1 + ""); //关数赋值
        mSelectWords = initSelectedWord();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(140, 140);
        mViewSelectedWords.removeAllViews();
        for (int i = 0; i < mSelectWords.size(); i++) {
            mViewSelectedWords.addView(mSelectWords.get(i).mViewButton, params);
        }
        mAllWords = initAllWord();
        mCustomGridView.updateData(mAllWords);
        //初始化已选择的
        // 更新数据 myGridView
        handlePlayButton();
    }

    /**
     * 加载指定关数的歌曲信息
     * @param stageIndex
     * @return
     */
    private Song loadStageSongInfo(int stageIndex){
        Song song = new Song();
        String[] stage = Const.SONG_INFO[stageIndex];
        song.setFileName(stage[Const.INDEX_FILE_NAME]);
        song.setSongName(stage[Const.INDEX_SONG_NAME]);
        return song;
    }

    /**
     * 初始化待选字
     * @return
     */
    private ArrayList<WordButton> initAllWord() {
        ArrayList<WordButton> data = new ArrayList<WordButton>();
        WordButton wb;
        //获取所有的待选文字
        String[] words = generateWords();
        for (int i = 0; i < words.length; i++) {
            wb = new WordButton();
            wb.mText = words[i];
            data.add(wb);
        }
        return data;
    }

    /**
     * 初始化已选择框
     * @return
     */
    private ArrayList<WordButton> initSelectedWord() {
        ArrayList<WordButton> wordButtons = new ArrayList<WordButton>();
        for (int i = 0; i < mCurrentSong.getNameLength(); i++) {
            View view = Util.getView(MainActivity.this, R.layout.custom_ui_gridview_item);
            final WordButton  holder = new WordButton();
            holder.mViewButton = (Button) view.findViewById(R.id.item_btn);
            holder.mViewButton.setTextColor(Color.WHITE);
            holder.mViewButton.setText("");
            holder.mIsVisiable = false;
            holder.mViewButton.setBackgroundResource(R.drawable.game_wordblank);
            holder.mViewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearSelectedAns(holder);
                }
            });
            wordButtons.add(holder);
        }
        return wordButtons;
    }

    /**
     * 待选择区内按钮点击事件
     * @param wordButton
     */
    @Override
    public void onWordButtonClick(WordButton wordButton) {
        //当过关界面未显示的时候才执行点击事件
        if(!mIsPassStageShown){
            moveToSelectedArea(wordButton);  //将选中按钮移动到已选择区
            int state = getAnsState();  //获取游戏状态
            handleGameState(state);  //处理游戏状态
        }
    }

    /**
     * 根据游戏状态触发事件
     * @param state
     */
    private void handleGameState(int state){
        switch (state){
            case ANS_LACK:
                //答案不完整
                clearSparkingColor();
                break;
            case ANS_WRONG:
                sparkSelectedWord();
                break;
            case ANS_RIGHT:
                //答案正确，加载过关界面
                handlePassStageEvent();
                break;
        }
    }

    private void handlePassStageEvent(){
        MusicPlayer.stopMusic();  //停止音乐播放
        //初始化过关界面，并播放出现动画
        mViewPassStage = (LinearLayout) this.findViewById(R.id.pass_view);
        mViewPassStage.setVisibility(View.VISIBLE);
        mScaleAnim = AnimationUtils.loadAnimation(this,R.anim.scale);
        mViewPassStage.startAnimation(mScaleAnim);
        mIsPassStageShown = true;

        //过关界面数据填充
        //1.关数
        mViewPassStageIndex = (TextView) findViewById(R.id.pass_stage_index);
        mViewPassStageIndex.setText(mCurrentStageIndex+1+"");
        //2.歌曲名称
        mViewPassStageSongName = (TextView) findViewById(R.id.pass_stage_song_name);
        mViewPassStageSongName.setText(mCurrentSong.getSongName());
        //3.金币增加 并 播放音效
        mCurrentCoins += getResources().getInteger(R.integer.pass_reward);
        mViewCurrentCoins.setText(mCurrentCoins +"");
        MusicPlayer.PlayTone(this, MusicPlayer.TONE_INDEX_COIN);
        //4.添加按钮监听
        mBtnNextStage = (ImageButton) findViewById(R.id.btn_next_stage);
        mBtnNextStage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleNextStageEvent();
            }
        });

        //关闭所有动画
        mPanAnim.cancel();
    }

    private void handleNextStageEvent(){
        if(mCurrentStageIndex+1 >= Const.SONG_INFO.length){
            //TODO 通关界面

        }else{
            initCurrentStageData(); //初始化当前下一关的数据
            mViewPassStage.setVisibility(View.GONE);  //隐藏过关界面
            mIsPassStageShown = false;  //标志量设置为false
        }
    }

    /**
     * 已选择框内文字闪烁
     */
    private void sparkSelectedWord(){
        //答案错误,答案闪烁
        TimerTask task = new TimerTask() {
            boolean colorFlag = false;  //颜色切换flag
            int sparkTime = 0;
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(sparkTime > SPARK_TIME){
                            return;
                        }
                        sparkTime++;
                        for(int i =0 ; i< mSelectWords.size(); i++){
                            mSelectWords.get(i).mViewButton.setTextColor(colorFlag==true?Color.RED:Color.WHITE);
                        }
                        colorFlag = !colorFlag;
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(task,1,200L);
    }

    /**
     * 清除文字闪烁之后的颜色
     */
    private void clearSparkingColor(){
        for(int i =0 ; i< mSelectWords.size(); i++){
            mSelectWords.get(i).mViewButton.setTextColor(Color.WHITE);
        }
    }

    /**
     * 获取当前游戏的状态
     * @return
     */
    private int getAnsState(){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < mSelectWords.size(); i++){
            sb.append(mSelectWords.get(i).mText);
            if("".equals(mSelectWords.get(i).mText)){
                return this.ANS_LACK;
            }
        }
        if(sb.toString().equals(mCurrentSong.getSongName())){
            return ANS_RIGHT;
        }else{
            return ANS_WRONG;
        }
    }

    /**
     * 清除已选择区内指定的wordButton
     * @param wordButton
     */
    private void clearSelectedAns(WordButton wordButton){
        //清除显示样式，并将可见性设为不可见
        wordButton.mText = "";
        wordButton.mViewButton.setText("");
        wordButton.mIsVisiable = false;
        //将待选区的可见性设为可见
        setWordButtonVisibility(mAllWords.get(wordButton.mIndex),View.VISIBLE);

    }

    /**
     * 将选中的wordButton移动到已选择区,再将点击的button隐藏。
     * @param wordButton 点击的wordButton
     */
    private void moveToSelectedArea(WordButton wordButton){
        boolean hasInsert = false;
        //遍历以选择的按钮栏，并给其赋值
        for (int i = 0 ; i < mSelectWords.size(); i++){
            WordButton wb = mSelectWords.get(i);
            if("".equals(wb.mText)){
                wb.mText = wordButton.mText;
                wb.mIndex = wordButton.mIndex;
                wb.mViewButton.setText(wordButton.mText);
                setWordButtonVisibility(wb,View.VISIBLE);
                hasInsert = true;
                break;
            }
        }
        //将所点击的wb隐藏,如果已经满了，就不插入
        if(hasInsert){
            setWordButtonVisibility(wordButton,View.INVISIBLE);
        }else{
            Toast.makeText(this,R.string.selected_full,Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 改变wordButton的可见性
     * @param wordButton
     * @param visibility
     */
    private void setWordButtonVisibility(WordButton wordButton,int visibility){
        wordButton.mIsVisiable = (visibility == View.VISIBLE ? true : false);
        wordButton.mViewButton.setVisibility(visibility);
    }

    //自定义AlertDialog事件相应
    // 删除错误答案
    private IAlertDialogButtonListener mBtnzOkDelAnsListener = new IAlertDialogButtonListener() {
        @Override
        public void onClick() {
            handleDelButton();
        }
    };

    // 答案提示
    private IAlertDialogButtonListener mBtnOkTipWordListener = new IAlertDialogButtonListener() {
        @Override
        public void onClick() {
            handleTipButton();
        }
    };

    // 金币不足
    private IAlertDialogButtonListener mBtnOkLackConisListener = new IAlertDialogButtonListener() {
        @Override
        public void onClick() {
            //TODO 完成金币充值功能
        }
    };

    private void showConfirmDialog(int id){
        switch (id){
            case ID_DIALOG_DELETE_WORD : {
                Util.showDialog(MainActivity.this, "您确认要花费" + getDelCost() + "金币消除一个错误答案？",mBtnzOkDelAnsListener);
                break;
            }
            case ID_DIALOG_LACK_COIN : {
                Util.showDialog(MainActivity.this, "金币不足，去商店补充？",mBtnOkLackConisListener);
                break;
            }
            case ID_DIALOG_TIP_WORD : {
                Util.showDialog(MainActivity.this, "您确认要花费" + getTipCost() + "金币提示一个正确答案？",mBtnOkTipWordListener);
                break;
            }
        }
    }
}
