import './App.css';
//import "antd/dist/antd.css";
import JoLPlayer, { callBackType, JoLPlayerRef, qualityKey } from "jol-player";
import React, { useRef, useEffect, useState } from "react";
import { Button, Input, Switch, Col, Row, Avatar, List, Skeleton, Space, Table, Tag } from "antd";
import type { ColumnsType } from 'antd/es/table';
import Column from 'antd/es/table/Column';
import { Line } from '@ant-design/plots';

export default function App() {
    const [theme, setTheme] = useState<string>("#ffb821");
    const videoRef = useRef<JoLPlayerRef>(null!);
    const [isShowMultiple, setIsShowMultiple] = useState<boolean>(true);
    const [initLoading, setInitLoading] = useState(true);

    const URL_BASE = 'http://localhost';
    const URL_EVENT_LIST = URL_BASE+'/api/loadData';
    const URL_SAVE_EVENT = URL_BASE +'/api/createEvent';
    const URL_CLEAN_DATA = URL_BASE +'/api/deleteAllData';
    const URL_EXPORT_DATA = URL_BASE +'/api/export';
    const URL_GAME_START = URL_BASE +'/api/gameStart';
    const URL_LOAD_GAME_TIMER = URL_BASE +'/api/loadGameTimer';


    /*const URL_EVENT_LIST = '../api/loadData';
    const URL_SAVE_EVENT = '../api/createEvent';
    const URL_CLEAN_DATA = '../api/deleteAllData';
    const URL_EXPORT_DATA = '../api/export';
    const URL_GAME_START = '../api/gameStart';
    const URL_LOAD_GAME_TIMER = '../api/loadGameTimer';*/

    const TEAM_HOME = 'HOME';
    const TEAM_AWAY = 'AWAY';

    const [timeMode, setTimeMode] = useState(1);
    const [timerSpan, setTimerSpan] = useState(0);
    const [videoSpan, setVideoSpan] = useState(16);

    const [currentVideoTime, setCurrentVideoTime] = useState(0);

    const [inpValue, setInpValue] = useState(''); 
    const [homeTeam, setHomeTeam] = useState('');
    const [awayTeam, setAwayTeam] = useState('');


    const [chartData, setChartData] = useState<EventDataType[]>([]);

    const [tableData, setTableData] = useState<EventDataType[]>([]);
    const [averageTableData, setAverageTableData] = useState<AverageDataType[]>([]);


    const [startTime, setStartTime] = useState(0);
    const [timerHour, setTimerHour] = useState('');
    const [timerMinute, setTimerMinute] = useState('');
    const [timerSecond, setTimerSecond] = useState('');

    const [currentTimerMinutes, setCurrentTimerMinutes] = useState(0);
    const [currentTimerSeconds, setCurrentTimerSeconds] = useState(0);


    interface EventDataType {
        team: String;
        event: String;
        minutes: number;
        seconds: number;
    }
    interface AverageDataType {
        event: String;
        averageAmount: Number;
        homeTeamAmount: Number;
        awayTeamAmount: Number;
    }

    const columns: ColumnsType<EventDataType> = [
        {
            title: 'Team',
            dataIndex: 'team',
            key: 'team',
            render: (text) => <span>{text}</span>,
        },
        {
            title: 'Event',
            dataIndex: 'event',
            key: 'event',
        },
        {
            title: 'Minutes',
            dataIndex: 'minutes',
            key: 'minutes',
        },
        {
            title: 'Seconds',
            dataIndex: 'seconds',
            key: 'seconds',
        }
    ];

    const averageColumns: ColumnsType<AverageDataType> = [
        {
            title: 'Key Indicators',
            dataIndex: 'event',
            key: 'event',
        },
        {
            title: 'Average',
            dataIndex: 'averageAmount',
            key: 'averageAmount',
        },
        {
            title: 'Home Team',
            dataIndex: 'homeTeamAmount',
            key: 'homeTeamAmount',
        },
        {
            title: 'Away Team',
            dataIndex: 'awayTeamAmount',
            key: 'awayTeamAmount',
        }
    ];


    const loadEventList = () => {
        fetch(URL_EVENT_LIST,
            {
                method: 'POST',
                headers: {
                    'content-type': 'application/x-www-form-urlencoded;charset=UTF-8',
                },
                body: new URLSearchParams({
                    'homeTeam': homeTeam,
                    'awayTeam': awayTeam
                })
            }
        )
            .then((res) => res.json())
            .then((res) => {
                setInitLoading(false);
                setTableData(res.allEvents);
                setAverageTableData(res.averageDataList);
                setChartData(res.chartPoints);
            });
    }

    const saveEvent = (team, event) => {
        // video mode
        var minutes = Math.floor(currentVideoTime / 60);
        var seconds = currentVideoTime % 60;

        if (timeMode == 2) {
            // timer mode
            minutes = currentTimerMinutes;
            seconds = currentTimerSeconds;
        }

        fetch(URL_SAVE_EVENT,
            {
                method: 'POST',
                headers: {
                    'content-type': 'application/json;charset=UTF-8',
                },
                body: JSON.stringify({
                    team,
                    event,
                    minutes,
                    seconds
                })
            }
        )
            .then((res) => res.json())
            .then((res) => {
                setInitLoading(false);
                loadEventList();
            });
    }

    useEffect(() => {
        loadEventList();
    }, []);

    const showInpValue = (e:any) => {
        setInpValue(e.target.value);
    }
    const showInpValue2 = () => {
        //message.info(inpValue);
    }
    const homeTeamChange = (e: any) => {
        setHomeTeam(e.target.value); 
    }
    const awayTeamChange = (e: any) => {
        setAwayTeam(e.target.value);
    }

    const cleanData = () => {
        fetch(URL_CLEAN_DATA,
            {
                method: 'POST',
                headers: {
                    'content-type': 'application/x-www-form-urlencoded;charset=UTF-8',
                },
                body: new URLSearchParams({
                    'homeTeam': homeTeam,
                    'awayTeam': awayTeam
                })
            }
        )
            .then((res) => res.json())
            .then((res) => {
                
            });
    }

    const exportData = () => {
        const exportUrl = URL_EXPORT_DATA + '?homeTeam=' + homeTeam + '&awayTeam=' + awayTeam;
        window.open(exportUrl);
    }

    const gameStart = () => {
        fetch(URL_GAME_START,
            {
                method: 'POST',
                headers: {
                    'content-type': 'application/x-www-form-urlencoded;charset=UTF-8',
                },
                body: new URLSearchParams({
                    'homeTeam': homeTeam,
                    'awayTeam': awayTeam
                })
            }
        )
            .then((res) => res.json())
            .then((res) => {
                
            });
    }

    const updateTimer = (st) => {

        const secs = Math.floor((Date.now() - st)/1000);
        const h = Math.floor(secs / 3600);
        setTimerHour(h < 10 ? '0' + h : '' + h);

        const m = Math.floor((secs - h*3600) / 60);
        setTimerMinute(m < 10 ? '0' + m : '' + m);

        const s = (secs - h * 3600 - m*60);
        setTimerSecond(s < 10 ? '0' + s : '' + s);

        setCurrentTimerMinutes(h * 3600 + m);
        setCurrentTimerSeconds(s);
    }

    const timeOutHandle = (st) => {
        console.log('update time:' + Date.now());
        updateTimer(st);
    }

    const loadGameTimer = () => {
        fetch(URL_LOAD_GAME_TIMER,
            {
                method: 'POST',
                headers: {
                    'content-type': 'application/x-www-form-urlencoded;charset=UTF-8',
                },
                body: new URLSearchParams({
                    'homeTeam': homeTeam,
                    'awayTeam': awayTeam
                })
            }
        )
            .then((res) => res.json())
            .then((res) => {
                updateTimer(res.startTime);
                window.setInterval(timeOutHandle, 1000, res.startTime);
            });
    }
   


    const onProgressMouseUp: callBackType = (val) => {
        console.log(`onProgressMouseUp`, val);
    };
    const onEndEd: callBackType = (val) => {
        console.log(`onEndEd`, val);
    };
    const onPause: callBackType = (val) => {
        console.log(`onPause`, val);
    };
    const onProgressMouseDown: callBackType = (val) => {
        console.log(`onProgressMouseDown`, val);
    };
    const onPlay: callBackType = (val) => {
        console.log(`onPlay`, val);
    };
    const onTimeChange: callBackType = (val) => {
        console.log(`onTimeChange`, val);
        setCurrentVideoTime(val.currentTime);
    };
    const onvolumechange: callBackType = (val) => {
        console.log(`onvolumechange`, val);
    };
    const onError = () => {
        console.log(`onError`);
    };
    const onQualityChange: callBackType<qualityKey> = (val) => {
        console.log(`onQualityChange`, val);
    };
    useEffect(() => {
        console.log(`videoRef.current`, videoRef.current);
    }, [videoRef.current]);

    const videoMethod = (status: string) => {
        if (status === "play") {
            videoRef.current.play();
        } else if (status === "Change Source") {
            //videoRef.current.setVideoSrc("https://www.dofactory.com/media/movie.mp4");
            videoRef.current.setVideoSrc(inpValue);

            
            videoRef.current.play();
        }
    };

    const getTeamName = (team) => {
        return team == TEAM_HOME ? homeTeam : awayTeam;
    }

    const doPass = (team: String) => {
        saveEvent(getTeamName(team), "Pass");
    };

    const doShoot = (team: String) => {
        saveEvent(getTeamName(team), "Shoot");
    };

    const doScore = (team: String, score: String) => {
        saveEvent(getTeamName(team), score);
    };

    const doAssist = (team: String) => {
        saveEvent(getTeamName(team), "Assist");
    };

    const changeMode = (mode) => {
        if (mode == 1) {
            setVideoSpan(16);
            setTimerSpan(0);
        } else if (mode == 2) {
            setVideoSpan(0);
            setTimerSpan(16);
        }
        setTimeMode(mode);
    }

    // There are many properties and methods, please refer to the documentation ...
    return (
        <div className="App">
            https://gs-files.oss-cn-hongkong.aliyuncs.com/okr/prod/file/2021/08/31/540p.mp4 <hr></hr>

            https://www.dofactory.com/media/movie.mp4 <hr></hr>

            https://gs-files.oss-cn-hongkong.aliyuncs.com/okr/test/file/2021/07/01/haiwang.mp4 <hr></hr>


            <Button onClick={() => changeMode(1)}>Video Mode</Button>
            <Button onClick={() => changeMode(2)}>Timer Mode</Button>


            <Row className="BoardHead">
                <div>GAA in-game Performance Analytics Dashboard</div>
            </Row>

            <Row>
                <Col span={8}>
                    <Row>
                        <div>
                            <div>
                                <label>Home:</label>
                                <Input
                                    size="small"
                                    value={homeTeam}
                                    onChange={homeTeamChange}
                                />

                                <label>Away:</label>
                                <Input
                                    size="small"
                                    value={awayTeam}
                                    onChange={awayTeamChange}
                                />

                                <Button onClick={() => cleanData()}>Clean Data</Button>
                                <Button onClick={() => exportData()}>Export Data</Button>
                            </div>

                            <Line
                                data={chartData}
                                height={260}
                                seriesField='team'
                                xField="minutes"
                                yField="score"
                                point={{ size: 5, shape: 'diamon' }}
                                color={['#19CDD7', '#DDB27C']}
                            />

                           </div>
                    </Row>

                    <Row>
                        <Col span={12}></Col>
                        <Col span={3}>Home</Col>
                        <Col span={3}>Away</Col>
                    </Row>

                    <Row>
                        <Table columns={averageColumns} dataSource={averageTableData} pagination={false} />
                    </Row>
                </Col>

                <Col>
                    <Row>
                        <Col span={timerSpan}>
                            <div>
                                <div>
                                    <span>{timerHour}</span>
                                    <span>:</span>
                                    <span>{timerMinute}</span>
                                    <span>:</span>
                                    <span>{timerSecond}</span>
                                </div>
                                <div>
                                    <Button onClick={() => gameStart()}>Game Start</Button>
                                    <Button onClick={() => loadGameTimer()}>Load Game Time</Button>
                                </div>
                             </div>
                        </Col>
                    </Row>

                    <div>
                        <Row>
                            <Col span={videoSpan}>
                                <Input
                                    size="large" placeholder="Video Source"
                                    value={inpValue} 
                                    onChange={showInpValue} 
                                    onPressEnter={showInpValue2}
                                /></Col>
                            <Col span={videoSpan}>
                                <Button onClick={() => videoMethod("Change Source")}>Change Source</Button>
                            </Col>
                        </Row>

                        <Row>
                            <Col span={videoSpan}>
                                <JoLPlayer
                                    ref={videoRef}
                                    onProgressMouseUp={onProgressMouseUp}
                                    onEndEd={onEndEd}
                                    onPause={onPause}
                                    onProgressMouseDown={onProgressMouseDown}
                                    onPlay={onPlay}
                                    onTimeChange={onTimeChange}
                                    onvolumechange={onvolumechange}
                                    onError={onError}
                                    onQualityChange={onQualityChange}
                                    option={{
                                        videoSrc:
                                            "https://gs-files.oss-cn-hongkong.aliyuncs.com/okr/prod/file/2021/08/31/540p.mp4",
                                        width: 550,
                                        height: 320,
                                        theme,
                                        poster:
                                            "https://gs-files.oss-cn-hongkong.aliyuncs.com/okr/prod/file/2021/08/31/1080pp.png",
                                        language: "en",
                                        isShowMultiple,
                                        pausePlacement: "center",
                                        quality: [
                                            {
                                                name: "FHD",
                                                url:
                                                    "https://gs-files.oss-cn-hongkong.aliyuncs.com/okr/prod/file/2021/08/31/720p.mp4"
                                            },
                                            {
                                                name: "HD",
                                                url:
                                                    "https://gs-files.oss-cn-hongkong.aliyuncs.com/okr/prod/file/2021/08/31/540p.mp4"
                                            },
                                            {
                                                name: "SD",
                                                url:
                                                    "https://gs-files.oss-accelerate.aliyuncs.com/okr/prod/file/2021/08/31/1630377480138360p.mp4"
                                            }
                                        ],
                                        isToast: true,
                                        isProgressFloat: true
                                    }}
                                />
                            </Col>
                        </Row>
                    </div>

                    <Row>
                        <Col>
                            <div className="eventTableClass">
                                <Table columns={columns} dataSource={tableData}
                                    pagination={false} bordered
                                    scroll={{ y: 200 }}
                                    />
                            </div>
                        </Col>

                        <Col>
                            <div>
                                <Row>
                                    <Col span={12}>HOME</Col>
                                    <Col span={12}>AWAY</Col>
                                </Row>
                                <Row>
                                    <Col span={12}><Button onClick={() => doPass(TEAM_HOME)}>Pass</Button></Col>
                                    <Col span={12}><Button onClick={() => doPass(TEAM_AWAY)}>Pass</Button></Col>
                                </Row>
                                <Row>
                                    <Col span={12}><Button onClick={() => doAssist(TEAM_HOME)}>Assist</Button></Col>
                                    <Col span={12}><Button onClick={() => doAssist(TEAM_AWAY)}>Assist</Button></Col>
                                </Row>
                                <Row>
                                    <Col span={12}><Button onClick={() => doShoot(TEAM_HOME)}>Shoot</Button></Col>
                                    <Col span={12}><Button onClick={() => doShoot(TEAM_AWAY)}>Shoot</Button></Col>
                                </Row>
                                <Row>
                                    <Col span={12}><Button onClick={() => doScore(TEAM_HOME, 'Score1')}>1 Score</Button></Col>
                                    <Col span={12}><Button onClick={() => doScore(TEAM_AWAY, 'Score1')}>1 Score</Button></Col>
                                </Row>
                                <Row>
                                    <Col span={12}><Button onClick={() => doScore(TEAM_HOME, 'Score3')}>3 Score</Button></Col>
                                    <Col span={12}><Button onClick={() => doScore(TEAM_AWAY, 'Score3')}>3 Score</Button></Col>
                                </Row>
                            </div>
                        </Col>
                    </Row>
                    
                </Col>

            </Row>

        </div>
    );
}
