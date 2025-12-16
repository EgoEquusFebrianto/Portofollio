from configparser import ConfigParser

def get_kafka_config(config_path, mode):
    configs = dict()
    config = ConfigParser()
    config.read(config_path)

    for (key, value) in config[mode].items():
        configs[key] = value

    return configs