# gunicorn_conf.py
import os
import sys
from datetime import time

bind = "0.0.0.0:8000"
workers = 4
worker_class = "uvicorn.workers.UvicornWorker"

log_dir = os.path.join(os.path.dirname(__file__), "logs")
os.makedirs(log_dir, exist_ok=True)

logconfig_dict = {
    "version": 1,
    "disable_existing_loggers": False,
    "formatters": {
        "verbose": {
            "format": "%(asctime)s [%(process)d] [%(levelname)s] %(name)s:%(lineno)d - %(message)s",
            "datefmt": "%Y-%m-%d %H:%M:%S"
        },
        "access": {
            "()": "uvicorn.logging.AccessFormatter",
            "fmt": '%(asctime)s [%(process)d] [%(levelname)s] %(client_addr)s - "%(request_line)s" %(status_code)s',
            "datefmt": "%Y-%m-%d %H:%M:%S"
        }
    },
    "handlers": {
        "default_file": {
            "class": "logging.handlers.TimedRotatingFileHandler",
            "filename": os.path.join(log_dir, "app.log"),
            "when": "midnight",
            "interval": 1,
            "backupCount": 7,
            "encoding": "utf-8",
            "formatter": "verbose",
            "atTime": time(0, 0, 0)
        },
        "console": {
            "class": "logging.StreamHandler",
            "stream": sys.stdout,
            "formatter": "verbose",
            "level": "INFO"
        },
        "access_file": {
            "class": "logging.handlers.TimedRotatingFileHandler",
            "filename": os.path.join(log_dir, "access.log"),
            "when": "midnight",
            "formatter": "access",
            "atTime": time(0, 0, 0)
        }
    },
    "loggers": {
        "app": {
            "handlers": ["default_file", "console"],
            "level": "INFO",
            "propagate": False
        },
        "gunicorn.access": {
            "handlers": ["access_file"],
            "level": "INFO",
            "propagate": False
        }
    },
    "root": {
        "handlers": ["default_file"],
        "level": "WARNING"
    }
}